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

//        SharedPreferences preferences = MainApplication.getAppContext().getSharedPreferences("COURSE_DATA", Context.MODE_PRIVATE);// 创建sp对象
//        String htmlToSubject = preferences.getString("HTML_TO_SUBJECT",null);  // 取出key为"HTML_TO_SUBJECT"的值，如果值为空，则将第二个参数作为默认值赋值

        String htmlToSubject = SharedPreferencesUtil.init(MainApplication.getAppContext(), "COURSE_DATA").getString("HTML_TO_SUBJECT", null);
        Log.e(TAG, "HTML_TO_SUBJECT: " + htmlToSubject);//HTML_TO_SUBJECT便是取出的数据了
        if (htmlToSubject == null) {
            return new ArrayList<>();
        }
        return parse(htmlToSubject);
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
                int day = courseinfo.get(i).getDay();
                String name = courseinfo.get(i).getName();
                String position = courseinfo.get(i).getPosition();
                String teacher = courseinfo.get(i).getTeacher();
                List<Integer> weeks = courseinfo.get(i).getWeeks();
                int start = courseinfo.get(i).getSections().get(0).getSection();
                int step = courseinfo.get(i).getSections().size();
                course.add(new MySubject( name, position, teacher, weeks, start, step, day, -1));
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
        private List<SectionTimesDTO> sectionTimes;

        public List<CourseInfosDTO> getCourseInfos() {
            return courseInfos;
        }

        public void setCourseInfos(List<CourseInfosDTO> courseInfos) {
            this.courseInfos = courseInfos;
        }

        public List<SectionTimesDTO> getSectionTimes() {
            return sectionTimes;
        }

        public void setSectionTimes(List<SectionTimesDTO> sectionTimes) {
            this.sectionTimes = sectionTimes;
        }

        public class CourseInfosDTO {
            private Integer day;
            private String name;
            private String position;
            private List<SectionsDTO> sections;
            private String teacher;
            private List<Integer> weeks;

            public Integer getDay() {
                return day;
            }

            public void setDay(Integer day) {
                this.day = day;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public List<SectionsDTO> getSections() {
                return sections;
            }

            public void setSections(List<SectionsDTO> sections) {
                this.sections = sections;
            }

            public String getTeacher() {
                return teacher;
            }

            public void setTeacher(String teacher) {
                this.teacher = teacher;
            }

            public List<Integer> getWeeks() {
                return weeks;
            }

            public void setWeeks(List<Integer> weeks) {
                this.weeks = weeks;
            }

            public class SectionsDTO {
                private Integer section;

                public Integer getSection() {
                    return section;
                }

                public void setSection(Integer section) {
                    this.section = section;
                }
            }

        }

        public class SectionTimesDTO {
            private String endTime;
            private Integer section;
            private String startTime;

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public Integer getSection() {
                return section;
            }

            public void setSection(Integer section) {
                this.section = section;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }
        }
    }
}