package annotation;

@MyAnnotation(annotationField = "This class is annotated.")
public class AnnotatedClass {
    Integer someIntValue;
    String str;

    public AnnotatedClass(){
        someIntValue = 123;
        str = "Hello world!";
    }

    public void getValues(){
        System.out.println("Integer value = " + someIntValue);
        System.out.println("String value = " + str);
    }
}
