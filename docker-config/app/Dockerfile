# Defines the image for the Spring Boot application
FROM maven:3.9-amazoncorretto-17

# Set the working directory
ENV APP_HOME /
WORKDIR $APP_HOME

# Copy the source code
COPY . .

# Build the application
RUN mvn clean package

# Run the application
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "target/wex-purchase-transaction-0.0.1-SNAPSHOT.jar"]

# Expose the application port
EXPOSE 2984
