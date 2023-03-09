package by.suhareva.adapterservice.integrationTest.MaperXml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class MapperXml {
    public static String writeValueAsString(Object object) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    public static <T> T readValueAsString(StringReader content, Class<T> valueType) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(valueType);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        T object = (T) unmarshaller.unmarshal(content);
        return object;
    }
}
