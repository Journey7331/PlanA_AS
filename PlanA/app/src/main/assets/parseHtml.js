function getTeacher(str_context){
  return str_context.split(" ")[0];
}

function getClassName(str_context){
  return str_context.split(" ")[1].split("(")[0];
}

function getWeeks(str_weeks){
    let weeks = [];
    let flag = 0;
    if (str_weeks.search("单") != -1) {
        flag = 1;
        str_weeks = str_weeks.replace("单", "");
    } else if (str_weeks.search("双") != -1) {
        flag = 2;
        str_weeks = str_weeks.replace("双", "");
    }
    str_weeks = str_weeks.replace("周", "").replace("(", "").replace(")", "").replace("-"," ").replace(",", " ");

    let startWeek = Number(str_weeks.split(' ')[0]);
    let endWeek = Number(str_weeks.split(' ')[1]);
    for (let i = startWeek; i <= endWeek; i++) {
        if (flag == 0) {
            weeks.push(i);
        } else if (flag == 1 && i % 2 == 1) {
            weeks.push[i];
        } else if (flag == 2 && i % 2 == 0) {
            weeks.push(i);
        }
    }
    return weeks;
}

function getRoom(str_room){
str_room = str_room.replace("单", "").replace("双", "").replace("周", "").replace("(", "").replace(")", "").replace("-"," ").replace(",", " ");
    return str_room.split(' ')[2];
}

function parseHtml(){
  let courseTable = document.getElementsByName("courseTableForCourseTable");
  let trs = courseTable[0].querySelectorAll("tr");
  let sectionTimes = [];
  let courseInfos = [];

  for(let i = 0; i < trs.length; i++){
    let tds = trs[i].querySelectorAll("td");

    for(let j = 0; j < tds.length; j++){
        let result = {};
        let titleContext = tds[j].getAttribute("title");
      let span = Number(tds[j].getAttribute("rowspan"));
        if(titleContext != null && span != null && titleContext.length > 1){
          let context = [];
          context = titleContext.split(";");
          result.teacher = getTeacher(context[0]);
          result.name = getClassName(context[0]);
          result.weeks = getWeeks(context[1]);
          result.room = getRoom(context[1]);
          result.day = j;
          result.start = i;
          result.span = span;
          courseInfos.push(result);
          if(context.length > 2){
            let res = {};
            res.teacher = result.teacher;
            res.name = result.name;
            res.day = j;
            res.start = i;
            res.span = span;
            res.weeks = getWeeks(context[2]);
            res.room = getRoom(context[2]);
            courseInfos.push(res);
          }
        }
    }
  }
  return {
    courseInfos: courseInfos
  }
}