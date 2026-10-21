/**
 * Sensor que mide la humedad ambiental (humedad del aire) en porcentaje.
 */
public class SensorHumedad extends Sensor {

    private static final double LIMITE_BAJO = 40.0;
    private static final double LIMITE_ALTO = 70.0;

    public SensorHumedad(String id, String ubicacion) {
        super(id, ubicacion, "%");
    }

    /**
     * Rechaza valores fuera de 0 a 100 %. Si el valor es válido,
     * la superclase se encarga de registrarlo.
     */
    @Override
    public boolean registrarMedicion(double valor) {
        if (valor < 0 || valor > 100) {
            return false;
        }
        return super.registrarMedicion(valor);
    }

    @Override
    public String evaluarMedicion() {
        double medicion = getUltimaMedicion();

        if (medicion < LIMITE_BAJO) {
            return "Humedad ambiental baja";
        } else if (medicion > LIMITE_ALTO) {
            return "Humedad ambiental alta";
        } else {
            return "Humedad ambiental adecuada";
        }
    }

}