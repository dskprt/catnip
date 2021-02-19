package com.github.dskprt.catnip.settings;

import com.github.dskprt.catnip.Catnip;
import com.github.dskprt.catnip.module.Module;
import com.github.dskprt.catnip.settings.annotations.*;
import com.github.dskprt.catnip.utils.MathUtils;
import org.apache.logging.log4j.util.TriConsumer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Settings {

    private static final XMLOutputFactory factory = XMLOutputFactory.newInstance();
    private static DocumentBuilder builder;

    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void iterate(Module module,
                               TriConsumer<ComboSetting, String, Field> comboConsumer,
                               TriConsumer<IntegerSetting, Integer, Field> integerConsumer,
                               TriConsumer<FloatSetting, Float, Field> floatConsumer,
                               TriConsumer<BooleanSetting, Boolean, Field> booleanConsumer,
                               TriConsumer<ColorSetting, Color, Field> colorConsumer) {
        for(Field f : module.getClass().getDeclaredFields()) {
            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
            if(!f.isAccessible()) f.setAccessible(true);

            for(Annotation a : f.getDeclaredAnnotations()) {
                try {
                    if(a instanceof ComboSetting) {
                        if(comboConsumer != null) comboConsumer.accept((ComboSetting) a, String.valueOf(f.get(o)), f);
                    } else if(a instanceof IntegerSetting) {
                        if(integerConsumer != null) integerConsumer.accept((IntegerSetting) a, (int) f.get(o), f);
                    } else if(a instanceof FloatSetting) {
                        if(floatConsumer != null) floatConsumer.accept((FloatSetting) a, (float) f.get(o), f);
                    } else if(a instanceof BooleanSetting) {
                        if(booleanConsumer != null) booleanConsumer.accept((BooleanSetting) a, (boolean) f.get(o), f);
                    } else if(a instanceof ColorSetting) {
                        if(colorConsumer != null) colorConsumer.accept((ColorSetting) a, (Color) f.get(o), f);
                    }
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int count(Module module) {
        int i = 0;

        for(Field f : module.getClass().getDeclaredFields()) {
            for(Annotation a : f.getDeclaredAnnotations()) {
                if((a instanceof ComboSetting) || (a instanceof IntegerSetting)
                        || (a instanceof FloatSetting) || (a instanceof BooleanSetting)
                        || (a instanceof ColorSetting)) {
                    i++;
                }
            }
        }

        return i;
    }

    // just why
    public static void setValue(Module module, String settingId, Object value) {
        iterate(module, (s, t, f) -> {
            if(s.id().equals(settingId)) {
                if(!f.isAccessible()) f.setAccessible(true);
                Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                try {
                    f.set(o, value);
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }, (s, t, f) -> {
            if(s.id().equals(settingId)) {
                if(!f.isAccessible()) f.setAccessible(true);
                Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                try {
                    f.set(o, value);
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }, (s, t, f) -> {
            if(s.id().equals(settingId)) {
                if(!f.isAccessible()) f.setAccessible(true);
                Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                try {
                    f.set(o, value);
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }, (s, t, f) -> {
            if(s.id().equals(settingId)) {
                if(!f.isAccessible()) f.setAccessible(true);
                Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                try {
                    f.set(o, value);
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }, (s, t, f) -> {
            if(s.id().equals(settingId)) {
                if(!f.isAccessible()) f.setAccessible(true);
                Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                try {
                    f.set(o, value);
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void load(Module module) {
        List<Field> fields = new LinkedList<>(Arrays.asList(module.getClass().getDeclaredFields()));
        File settingsFile = new File(Catnip.SETTINGS_DIR, String.format("%s.xml", module.getId()));

        if(!settingsFile.exists()) {
            return;
        }

        Document document;

        try {
            document = builder.parse(settingsFile);
            document.getDocumentElement().normalize();
        } catch(SAXException | IOException e) {
            e.printStackTrace();
            return;
        }

        module.setEnabled(Boolean.getBoolean(document.getElementsByTagName("enabled").item(0).getTextContent()));

        NodeList nl = document.getElementsByTagName("setting");

        for(int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE) return;

            Element e = (Element) node;
            Iterator<Field> iterator = fields.iterator();

            switch(e.getAttribute("type")) {
                case "str":
                    while(iterator.hasNext()) {
                        Field f = iterator.next();

                        if(f.isAnnotationPresent(ComboSetting.class)) {
                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            try {
                                f.set(o, e.getTextContent());
                            } catch(IllegalAccessException ex) {
                                ex.printStackTrace();
                            }

                            iterator.remove();
                        }
                    }
                    break;
                case "int":
                    while(iterator.hasNext()) {
                        Field f = iterator.next();

                        if(f.isAnnotationPresent(IntegerSetting.class)) {
                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            try {
                                f.set(o, Integer.valueOf(e.getTextContent()));
                            } catch(IllegalAccessException ex) {
                                ex.printStackTrace();
                            }

                            iterator.remove();
                        }
                    }
                    break;
                case "bool":
                    while(iterator.hasNext()) {
                        Field f = iterator.next();

                        if(f.isAnnotationPresent(BooleanSetting.class)) {
                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            try {
                                f.set(o, Boolean.valueOf(e.getTextContent()));
                            } catch(IllegalAccessException ex) {
                                ex.printStackTrace();
                            }

                            iterator.remove();
                        }
                    }
                    break;
                case "float":
                    while(iterator.hasNext()) {
                        Field f = iterator.next();

                        if(f.isAnnotationPresent(FloatSetting.class)) {
                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            try {
                                f.set(o, Float.valueOf(e.getTextContent()));
                            } catch(IllegalAccessException ex) {
                                ex.printStackTrace();
                            }

                            iterator.remove();
                        }
                    }
                    break;
                case "color":
                    while(iterator.hasNext()) {
                        Field f = iterator.next();

                        if(f.isAnnotationPresent(ColorSetting.class)) {
                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            try {
                                f.set(o, new Color(Integer.parseInt(e.getAttribute("red")),
                                        Integer.parseInt(e.getAttribute("green")),
                                        Integer.parseInt(e.getAttribute("blue")),
                                        Integer.parseInt(e.getAttribute("alpha"))));
                            } catch(IllegalAccessException ex) {
                                ex.printStackTrace();
                            }

                            iterator.remove();
                        }
                    }
                    break;
            }
        }
    }

    public static void save(Module module) {
        List<Field> fields = new LinkedList<>(Arrays.asList(module.getClass().getDeclaredFields()));
        File settingsFile = new File(Catnip.SETTINGS_DIR, String.format("%s.xml", module.getId()));

        if(!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try(FileOutputStream fos = new FileOutputStream(settingsFile)) {
            XMLStreamWriter writer = factory.createXMLStreamWriter(fos, "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");

            writer.writeStartElement("module");
            {
                writer.writeCharacters("\n    ");
                writer.writeStartElement("enabled");
                writer.writeCharacters(String.valueOf(module.isEnabled()));
                writer.writeEndElement();
                writer.writeCharacters("\n    ");

                writer.writeStartElement("settings");
                {
                    Iterator<Field> iterator = fields.iterator();

                    while(iterator.hasNext()) {
                        Field f = iterator.next();
                        writer.writeCharacters("\n        ");

                        if(f.getType() == String.class) {
                            ComboSetting setting = f.getAnnotation(ComboSetting.class);
                            if(setting == null) continue;

                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            writer.writeStartElement("setting");
                            {
                                writer.writeAttribute("type", "str");
                                writer.writeAttribute("id", setting.id());
                                writer.writeAttribute("name", setting.name());
                                writer.writeCharacters(String.valueOf(f.get(o)));
                            }
                            writer.writeEndElement();

                            iterator.remove();
                        } else if(f.getType() == int.class) {
                            IntegerSetting setting = f.getAnnotation(IntegerSetting.class);
                            if(setting == null) continue;

                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            writer.writeStartElement("setting");
                            {
                                writer.writeAttribute("type", "int");
                                writer.writeAttribute("id", setting.id());
                                writer.writeAttribute("name", setting.name());
                                writer.writeCharacters(String.valueOf(f.get(o)));
                            }
                            writer.writeEndElement();

                            iterator.remove();
                        } else if(f.getType() == boolean.class) {
                            BooleanSetting setting = f.getAnnotation(BooleanSetting.class);
                            if(setting == null) continue;

                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            writer.writeStartElement("setting");
                            {
                                writer.writeAttribute("type", "bool");
                                writer.writeAttribute("id", setting.id());
                                writer.writeAttribute("name", setting.name());
                                writer.writeCharacters(String.valueOf(f.get(o)));
                            }
                            writer.writeEndElement();

                            iterator.remove();
                        } else if(f.getType() == float.class) {
                            FloatSetting setting = f.getAnnotation(FloatSetting.class);
                            if(setting == null) continue;

                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            writer.writeStartElement("setting");
                            {
                                writer.writeAttribute("type", "float");
                                writer.writeAttribute("id", setting.id());
                                writer.writeAttribute("name", setting.name());
                                writer.writeCharacters(String.valueOf(MathUtils.round((float)f.get(o), setting.decimal())));
                            }
                            writer.writeEndElement();

                            iterator.remove();
                        } else if(f.getType() == Color.class) {
                            ColorSetting setting = f.getAnnotation(ColorSetting.class);
                            if(setting == null) continue;

                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;
                            if(!f.isAccessible()) f.setAccessible(true);

                            writer.writeStartElement("setting");
                            {
                                writer.writeAttribute("type", "color");
                                writer.writeAttribute("id", setting.id());
                                writer.writeAttribute("name", setting.name());

                                Color color = (Color) f.get(o);
                                writer.writeAttribute("red", String.valueOf(color.getRed()));
                                writer.writeAttribute("green", String.valueOf(color.getGreen()));
                                writer.writeAttribute("blue", String.valueOf(color.getBlue()));
                                writer.writeAttribute("alpha", String.valueOf(color.getAlpha()));
                            }
                            writer.writeEndElement();

                            iterator.remove();
                        }
                    }
                }
                writer.writeCharacters("\n    ");
                writer.writeEndElement();
            }
            writer.writeCharacters("\n");
            writer.writeEndElement();
            writer.writeEndDocument();

            writer.flush();
            writer.close();
        } catch(IOException | XMLStreamException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
