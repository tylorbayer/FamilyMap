package fms.handler;


public class ObjectCoder {
    public class FMSNames {
        String[] data;

        public FMSNames(String[] names) {
            this.data = names;
        }

        public String[] getNames() {
            return data;
        }
    }

    public class FMSLocations {
        Location[] data;

        public FMSLocations(Location[] data) {

            this.data = data;
        }

        public Location[] getData() {
            return data;
        }

        public class Location {
            String country;
            String city;
            double latitude;
            double longitude;

            public Location(String country, String city, int latitude, int longitude) {
                this.country = country;
                this.city = city;
                this.latitude = latitude;
                this.longitude = longitude;
            }

            public String getCountry() {
                return country;
            }

            public String getCity() {
                return city;
            }

            public double getLatitude() {
                return latitude;
            }

            public double getLongitude() {
                return longitude;
            }
        }
    }
}
