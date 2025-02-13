package com.naebom.stroke.naebom.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class HospitalService {

    @Value("${google.maps.api-key}") // application.yml에서 API KEY 가져오기
    private String apiKey;

    private final RestTemplate restTemplate;

    public HospitalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getNearbyHospitals(double userLat, double userLng) {
        // Google Places API 요청 URL 생성
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .queryParam("location", userLat + "," + userLng)
                .queryParam("radius", 10000)
                .queryParam("type", "hospital")
                .queryParam("language", "ko")
                .queryParam("keyword", "뇌졸중")
                .queryParam("key", apiKey)
                .toUriString();

        System.out.println("Google Places API 요청: " + url);

        // Google API
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);


        if (response == null || !response.containsKey("results")) {
            return List.of();
        }

        List<Map<String, Object>> hospitals = (List<Map<String, Object>>) response.get("results");

        return processHospitalData(hospitals, userLat, userLng);
    }

    private List<Map<String, Object>> processHospitalData(List<Map<String, Object>> hospitals, double userLat, double userLng) {
        List<Map<String, Object>> processedHospitals = new ArrayList<>();

        for (Map<String, Object> hospital : hospitals) {
            Map<String, Object> geometry = (Map<String, Object>) hospital.get("geometry");
            if (geometry == null || !geometry.containsKey("location")) continue;

            Map<String, Object> location = (Map<String, Object>) geometry.get("location");
            if (location == null || !location.containsKey("lat") || !location.containsKey("lng")) continue;

            double hospitalLat = (double) location.get("lat");
            double hospitalLng = (double) location.get("lng");

            // 사용자와 병원 거리 계산 (단위: km)
            double distance = calculateDistance(userLat, userLng, hospitalLat, hospitalLng);

            Map<String, Object> hospitalData = new HashMap<>();
            hospitalData.put("name", hospital.get("name")); // 병원이름
            hospitalData.put("latitude", hospitalLat); // 병원 위도
            hospitalData.put("longitude", hospitalLng); // 병원 경도
            hospitalData.put("address", hospital.get("formatted_address"));
            hospitalData.put("phone_number", hospital.get("formatted_phone_number"));
            hospitalData.put("opening_hours", hospital.get("opening_hours"));
            hospitalData.put("distance_km", String.format("%.2f", distance)); // 사용자와 병원의 거리 (소수점 2자리)

            processedHospitals.add(hospitalData);
        }

        // 거리순 정렬 (가까운 병원부터)
        processedHospitals.sort(Comparator.comparingDouble(h -> Double.parseDouble((String) h.get("distance_km"))));

        return processedHospitals;
    }

    // Haversine 공식을 이용한 거리 계산 (단위: km)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (단위: km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // 결과값 (단위: km)
    }
}
