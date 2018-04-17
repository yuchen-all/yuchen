//时间格式化 useage:formatDate("1408609262000","yyyy-mm-dd");
function formatDate(timestamp,format){
    if(typeof timestamp!=="undefined"){
        var date = new Date(Number(timestamp)),
            oDate="",
            year = date.getFullYear(),
            month = date.getMonth()+1,
            day = date.getDate(),
            hours = date.getHours(),
            minutes = date.getMinutes(),
            seconds = date.getSeconds();
        var time = [month, day, hours, minutes, seconds];
        for(i in time){
            if(time[i]<10){
                time[i]="0"+time[i];
            }
        }
        if(typeof format==="undefined"){
            oDate=year + "-" + time[0] + "-" + time[1];
            return oDate;
        }else{
            switch (format) {
                case "yyyy-mm-dd HH:mm:ss" :
                    oDate = year + "-" + time[0] + "-" + time[1] + " " + time[2] + ":"
                        + time[3] + ":" + time[4];
                    break;
                case "yyyy-mm-dd HH:mm" :
                    oDate = year + "-" + time[0] + "-" + time[1] + " " + time[2] + ":"
                        + time[3];
                    break;
                case "yyyy-mm-dd" :
                    oDate= year + "-" + time[0] + "-" + time[1];
                    break;
                case "yyyy-mm" :
                    oDate= year +  "-" + time[0];
                    break;
                case "HH:mm:ss" :
                    oDate = time[2] + ":" + time[3] + ":" + time[4];
                    break;
                case "HH:mm" :
                    oDate = time[2] + ":"+ time[3];
                    break;
                default :
                    break;
            }
            return oDate;
        }
        return;
    }else{
        throw new Error("formatDate():timestamp is not defined");
    }
}