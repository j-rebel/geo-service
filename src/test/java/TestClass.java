import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class TestClass {

    @Test
    public void returns_ru_location_with_ru_ip() {
        Location location = new GeoServiceImpl().byIp("172.0.0.0");
        Assertions.assertEquals(location.getCountry(), Country.RUSSIA);
    }

    @Test
    public void returns_nonru_location_with_nonru_ip() {
        Location location = new GeoServiceImpl().byIp("96.0.0.0");
        Assertions.assertEquals(location.getCountry(), Country.USA);
    }

    @Test
    public void returns_ru_string_with_ru() {
        String message = new LocalizationServiceImpl().locale(Country.RUSSIA);
        System.out.println(message);
        Assertions.assertFalse(message.matches("[a-zA-Z]+"));
    }

    @Test
    public void returns_nonru_string_with_nonru() {
        String message = new LocalizationServiceImpl().locale(Country.USA);
        System.out.println(message);
        Assertions.assertTrue(message.matches("[a-zA-Z]+"));
    }

    @Test
    public void send_russian_text_with_russian_ip() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("test")).thenReturn(new Location(null, Country.RUSSIA, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Сообщение на русском");
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "test");
        String message = messageSender.send(headers).replace("Отправлено сообщение: ", "");;
        Assertions.assertFalse(message.matches("[a-zA-Z]+"));
    }

    @Test
    public void send_english_text_with_non_russian_ip() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("test")).thenReturn(new Location(null, Country.USA, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Message in English");
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "test");
        String message = messageSender.send(headers).replace("Отправлено сообщение: ", "");
        Assertions.assertFalse(message.matches("[a-zA-Z]+"));
    }






}
