package annotation;

public class Launcher {
    public static void main(String[] args){
        //создаем класс имеющий аннотацию
        AnnotatedClass aClass = new AnnotatedClass();
        //получаем объект, описывающий наш класс AnnotatedClass
        Class c = aClass.getClass();
        //получаем аннотацию у объекта-описателя, приводим ее к типу нашей аннотации
        MyAnnotation a = (MyAnnotation) c.getAnnotation(MyAnnotation.class);
        //Выводим на консоль поле аннотации, содержащее значение для нашего класса
        System.out.println(a.annotationField());

    }
}
