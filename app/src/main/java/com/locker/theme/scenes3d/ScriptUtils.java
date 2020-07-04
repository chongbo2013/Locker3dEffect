package com.locker.theme.scenes3d;



import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import org.w3c.dom.Text;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * xml解析运算符，增加动画构建的灵活性
 * Created by xff on 2017/12/21.
 */

public class ScriptUtils {

    //获取JavaScript执行引擎
    private static  ScriptEngine se = new ScriptEngineManager().getEngineByName("rhino");
    public static ScriptEngine se() {
        if(se==null){
            se = new ScriptEngineManager().getEngineByName("rhino");
        }
        return se;
    }
    public static  <T> T getValue(String str,Class<T> type){
        if(TextUtils.isEmpty(str))
            return (T)((Object)Float.parseFloat(0+""));
        if(Color.class.isAssignableFrom(type) ){
            String[] chars=str.split(",");
            if(chars.length==4){
                return (T) new Color(Float.parseFloat(chars[0]),Float.parseFloat(chars[1]),Float.parseFloat(chars[2]),Float.parseFloat(chars[3]));
            }
            return null;
        }

        String js = putFloatAttarts(str);
        if(js==null){
            return (T)((Object)Float.parseFloat(0+""));
        }
        try {
            Object obj=se().eval(js+";");
            if(obj instanceof Double){
                String fobj=obj+"";
                return (T)((Object)Float.parseFloat(fobj));
            }else if(obj instanceof Integer){
                String fobj=obj+"";
                return (T)((Object)Float.parseFloat(fobj));
            }else if(obj instanceof Float){
                return (T)(obj);
            }

        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String  putFloatAttarts(String str){
        if(str.contains("screenWidth")){
            str= str.replaceAll("screenWidth",Gdx.graphics.getWidth()+"");
        }
        if(str.contains("screenHeight")){
            str= str.replaceAll("screenHeight",Gdx.graphics.getHeight()+"");
        }
        return str;
    }


    /**
     * 尽可能使用编译，加快速度，因为编译后已经生成机器语言
     *
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public static void test() throws ScriptException,NoSuchMethodException {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("rhino");
        if (se != null) {
            //   进行简单的脚本表达式计算：
            Object ret = se.eval("3+4;");
            System.out.println(ret);

            //使用Bindings上下文计算
            Bindings bindings = se.createBindings();
            bindings.put("user", new User("张三", 19));
            ret = se.eval("print(user.getName());" +
                    "if(user.age>=18) " +
                    "'已成年'; " +
                    "else " +
                    "'未成年';", bindings);
            System.out.println(ret);

            // 使用编译方式进行计算。
            Compilable ce = (Compilable) se;
            String script = "println(user.getName()+'的年龄为'+user.getAge());if(user.age>=18) '已成年'; else '未成年';";
            CompiledScript cs = ce.compile(script);
            bindings = se.createBindings();
            bindings.put("user", new User("张三", 19));
            ret = cs.eval(bindings);
            System.out.println(ret);


//            解释方式执行javascript函数
            script = "function sum(a,b) { return a+b; }";
            se.eval(script);
            ret = se.eval("sum(3,4)");
            System.out.println(ret);

            // 编译方式执行javascript函数
            ce = (Compilable) se;
            script = "function sum(a,b) { return a+b; } sum(a,b);";
            cs = ce.compile(script);
            bindings = se.createBindings();
            bindings.put("a", 3);
            bindings.put("b", 4);
            ret = cs.eval(bindings);
            System.out.println(ret);

            //JS函数调用的高级用法
            // JavaScript code in a String
             script = "function hello(name) { print('Hello, ' + name); }";
            // evaluate script
            se.eval(script);
            // javax.script.Invocable is an optional interface.
            // Check whether your script engine implements or not!
            // Note that the JavaScript engine implements Invocable interface.
            Invocable inv = (Invocable) se;
            // invoke the global function named "hello"

            ret=inv.invokeFunction("hello", "Scripting!!" );
            System.out.println(ret);

//            函数使用高级用法2，调用对象的方法。

            // JavaScript code in a String. This code defines a script object 'obj'
            // with one method called 'hello'.
             script = "var obj = new Object(); obj.hello = function(name) { print('Hello, ' + name); }";
            // evaluate script
            se.eval(script);
            // javax.script.Invocable is an optional interface.
            // Check whether your script engine implements or not!
            // Note that the JavaScript engine implements Invocable interface.
            inv = (Invocable) se;
            // get script object on which we want to call the method
            Object obj = se.get("obj");
            // invoke the method named "hello" on the script object "obj"
            ret=inv.invokeMethod(obj, "hello", "Script Method !!" );
            System.out.println(ret);
        }


    }



    public static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
