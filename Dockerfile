FROM openjdk:17
MAINTAINER briantran
COPY target/amenity-reservation-system-0.0.1-SNAPSHOT.jar amenity-reservation-system.jar
ENTRYPOINT ["java", "-jar", "/amenity-reservation-system.jar"]