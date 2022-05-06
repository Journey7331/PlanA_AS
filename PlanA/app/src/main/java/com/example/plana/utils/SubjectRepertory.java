package com.example.plana.utils;

import android.util.Log;

import com.example.plana.base.MainApplication;
import com.example.plana.bean.MySubject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: PlanA
 * @description:
 */

public class SubjectRepertory {

    private static final String TAG = "SubjectRepertory";

    /**
     * 加载课程数据
     *
     * @return List<MySubject>
     */
    public static List<MySubject> loadDefaultSubjects() {
        // String json = "sample data"
        String json = "{\"courseInfos\":[{\"teacher\":\"王瑞平\",\"name\":\"概率论与数理统计\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"6304\",\"day\":1,\"start\":1,\"span\":2},{\"teacher\":\"闫昱\",\"name\":\"数据库系统概论\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"4314\",\"day\":3,\"start\":1,\"span\":2},{\"teacher\":\"王帅\",\"name\":\"算法设计与分析\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"6104\",\"day\":2,\"start\":3,\"span\":2},{\"teacher\":\"施汉明\",\"name\":\"线性代数\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"6302\",\"day\":3,\"start\":3,\"span\":2},{\"teacher\":\"王翔\",\"name\":\"通用学术英语A\",\"weeks\":[5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"4501\",\"day\":4,\"start\":3,\"span\":2},{\"teacher\":\"王翔\",\"name\":\"通用学术英语A\",\"day\":4,\"start\":3,\"span\":2,\"weeks\":[1,2,3,4],\"room\":\"5309\"},{\"teacher\":\"翁雯\",\"name\":\"可视化程序设计\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"4105\",\"day\":2,\"start\":6,\"span\":2},{\"teacher\":\"左坤\",\"name\":\"男篮-2\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"篮球场东北\",\"day\":4,\"start\":6,\"span\":2},{\"teacher\":\"唐姗\",\"name\":\"Java程序设计\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"7100\",\"day\":5,\"start\":6,\"span\":2},{\"teacher\":\"王胜利\",\"name\":\"大学物理B\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"4113\",\"day\":2,\"start\":8,\"span\":2},{\"teacher\":\"郭灿希\",\"name\":\"毛泽东思想和中国特色社会主义理论体系概论I\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],\"room\":\"8302\",\"day\":3,\"start\":8,\"span\":3},{\"teacher\":\"张天祺\",\"name\":\"形势与政策（模块4）\",\"weeks\":[11,12],\"room\":\"4201\",\"day\":4,\"start\":8,\"span\":3},{\"teacher\":\"程路\",\"name\":\"英美影视语言与文化\",\"weeks\":[1,2,3,4,5,6,7,8,9,10,11],\"room\":\"5116\",\"day\":4,\"start\":11,\"span\":3}]}";

//        SharedPreferences preferences = MainApplication.getAppContext().getSharedPreferences("COURSE_DATA", Context.MODE_PRIVATE);// 创建sp对象
//        String htmlToSubject = preferences.getString("HTML_TO_SUBJECT",null);  // 取出key为"HTML_TO_SUBJECT"的值，如果值为空，则将第二个参数作为默认值赋值

//        String htmlToSubject = SharedPreferencesUtil.init(MainApplication.getAppContext(), "COURSE_DATA").getString("HTML_TO_SUBJECT", null);
//        Log.e(TAG, "HTML_TO_SUBJECT: " + htmlToSubject);//HTML_TO_SUBJECT便是取出的数据了
//        if (htmlToSubject == null) {
//            return new ArrayList<>();
//        }
        return parse(json);
    }

    /**
     * 对json字符串进行解析
     *
     * @param parseString 带解析的json字符串
     * @return 解析后的课程列表
     */
    protected static List<MySubject> parse(String parseString) {
        List<MySubject> course = new ArrayList<>();
        Gson gson = new Gson();
        MysubjectDTO jsonObject = gson.fromJson(parseString, MysubjectDTO.class);
        try {
            List<MysubjectDTO.CourseInfosDTO> courseinfo = jsonObject.getCourseInfos();
            for (int i = 0; i < courseinfo.size(); i++) {
                String teacher = courseinfo.get(i).getTeacher();
                String name = courseinfo.get(i).getName();
                List<Integer> weeks = courseinfo.get(i).getWeeks();
                String room = courseinfo.get(i).getRoom();
                int day = courseinfo.get(i).getDay();
                int start = courseinfo.get(i).getStart();
                int step = courseinfo.get(i).getSpan();
                course.add(new MySubject( name, room, teacher, weeks, start, step, day, -1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    /**
     * 实例化课程对象
     */
    public class MysubjectDTO {
        private List<CourseInfosDTO> courseInfos;

        public List<CourseInfosDTO> getCourseInfos() {
            return courseInfos;
        }

        public void setCourseInfos(List<CourseInfosDTO> courseInfos) {
            this.courseInfos = courseInfos;
        }

        public class CourseInfosDTO {
            private String teacher;
            private String name;
            private List<Integer> weeks;
            private String room;
            private Integer day;
            private Integer start;
            private Integer span;

            public String getTeacher() {
                return teacher;
            }

            public void setTeacher(String teacher) {
                this.teacher = teacher;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Integer> getWeeks() {
                return weeks;
            }

            public void setWeeks(List<Integer> weeks) {
                this.weeks = weeks;
            }

            public String getRoom() {
                return room;
            }

            public void setRoom(String room) {
                this.room = room;
            }

            public Integer getDay() {
                return day;
            }

            public void setDay(Integer day) {
                this.day = day;
            }

            public Integer getStart() {
                return start;
            }

            public void setStart(Integer start) {
                this.start = start;
            }

            public Integer getSpan() {
                return span;
            }

            public void setSpan(Integer span) {
                this.span = span;
            }

        }
    }
}