FROM openjdk:18-alpine
MAINTAINER wishlist-challenge
RUN mkdir /apps
COPY build/libs/wishlist-challenge-1.0-SNAPSHOT.jar /apps/wishlist.jar
ENTRYPOINT ["java", "-jar", "/apps/wishlist.jar"]