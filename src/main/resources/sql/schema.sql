CREATE TABLE IF NOT EXISTS places
(
    id   VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longtitude DOUBLE NOT NULL,
    place_name VARCHAR(200) NOT NULL,
    description VARCHAR
);