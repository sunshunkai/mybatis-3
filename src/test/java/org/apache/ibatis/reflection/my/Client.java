package org.apache.ibatis.reflection.my;

import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.submitted.basetest.User;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ssk
 * @date 2021/3/11
 */
public class Client {

    @Test
    public void test(){
      //创建ObjectFactory工厂类
      ObjectFactory objectFactory = new DefaultObjectFactory();
      //获取实例化的目标对象
      User user = objectFactory.create(User.class);
      //创建ObjectWrapperFactory工厂类
      ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
      //创建ReflectorFactory工厂类
      ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
      MetaObject metaObject = MetaObject.forObject(user, objectFactory, objectWrapperFactory, reflectorFactory);

      //使用反射工具类将行数据转换成POJO类
      BeanWrapper objectWrapper = (BeanWrapper) metaObject.getObjectWrapper();

      //模拟数据库行数据转化成对象
      //1.模拟从数据库读取数据
      Map<String, Object> dbResult = new HashMap<>();
      dbResult.put("id", 1);
      dbResult.put("name", "张三");
      //2.模拟映射关系
      Map<String, String> mapper = new HashMap<String, String>();
      mapper.put("id", "id");
      mapper.put("name", "name");

      Set<Map.Entry<String, String>> entrySet = mapper.entrySet();
      for (Map.Entry<String, String> colInfo : entrySet) {
        String propName = colInfo.getKey();
        Object propValue = dbResult.get(colInfo.getValue());
        PropertyTokenizer proTokenizer = new PropertyTokenizer(propName);
        objectWrapper.set(proTokenizer, propValue);
      }
    }


}
