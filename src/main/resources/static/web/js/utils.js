//==========================form S================================================
//必填
var check_required = function (str) {
    if (str && str.trim().length > 0) {
        return true;
    }
    return false;
};

//检测手机  
var check_tel = function (str) {
    if (str) {
        var pattPhone = /^1\d{10}$/;
        return pattPhone.test(str);
    }
    return false;
};

//检测手机
var s_substring = function (str, s, e) {
    if (check_required(str) && str.length > e) {
        return str.substring(s, e);
    }
    return str;
};

//字符串长度计算
var s_Length = function (str) {
    //中文1，英文0.5
    var realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) {
            realLength += 0.5;
        } else {
            realLength += 1;
        }
    }
    return realLength;
};

//字符串长度计算
var titleSymbolReg = /[“”！？￥（）：；。，、‘’]/;
var s_TitleLength = function (str) {
    //中文1，英文0.5
    var realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) {
            realLength += 0.5;
        } else {
            //log("str[i]:"+str[i]);
            if (titleSymbolReg.test(str[i])) {
                realLength += 1;
            } else {
                realLength += 1;
            }
        }
    }
    return realLength;
};

//Fuzzy matching search
function matchString(subStr, str) {
    var reg = eval("/" + subStr + "/ig");
    return reg.test(str);
}

//==========================form E================================================

//log
var log = function (str) {
    console.log(str);
};

//========================timeAgo S===============================
var TIME_MINUTE = 60;
var TIME_HOUR = TIME_MINUTE * 60;
var TIME_DAY = TIME_HOUR * 24;
var newsTime = function (time) {
    var offset_second = parseInt((new Date().getTime() - time) / 1000);
    if (offset_second <= 0) {
        offset_second = 1;
    }
    if (offset_second < TIME_HOUR) {
        return Math.floor(offset_second / TIME_MINUTE) + "分钟前";
    } else if (offset_second < TIME_DAY) {
        return Math.floor(offset_second / TIME_HOUR) + "小时前";
    }

    var day1 = new Date(), day2 = new Date(day1.getTime()), tmp = new Date(time);
    day2.setFullYear(tmp.getFullYear());
    day2.setMonth(tmp.getMonth());
    day2.setDate(tmp.getDate());

    var offset_day = parseInt((day1.getTime() - day2.getTime()) / 1000 / TIME_DAY);
    if (offset_day < 30) {
        return offset_day + "天前";
    } else if (offset_day < 365) {
        return Math.floor(offset_day / 30) + "月前";
    } else {
        return Math.floor(offset_day / 365) + "年前";
    }
};
//========================timeAgo E===============================

//========================Date format S===============================
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}
/**
 *转换日期对象为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
var getFormatDate = function (date, pattern) {
    if (date == undefined) {
        date = new Date();
    }
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd ";
    }
    return date.format(pattern);

}
/**
 *转换long值为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
var getFormatDateByLong = function (l, pattern) {
    return getFormatDate(new Date(l), pattern);
}
/**
 * 转换日期字符串为date对象
 * @param dateStr
 */
var getDateByStr = function (dateStr) {
    return new Date(Date.parse(dateStr));
}
//========================Date format E===============================


//==========================HashMap S================================================
//定义map    
function Map() {
    this.container = {};
}
//将key-value放入map中    
Map.prototype.put = function (key, value) {
    try {
        if (key != null && key != "")
            this.container[key] = value;
    } catch (e) {
        return e;
    }
};

//根据key从map中取出对应的value    
Map.prototype.get = function (key) {
    try {
        return this.container[key];
    } catch (e) {
        return e;
    }
};

//判断map中是否包含指定的key    
Map.prototype.containsKey = function (key) {
    try {
        for (var p in this.container) {
            if (p == key)
                return true;
        }
        return false;

    } catch (e) {
        return e;
    }

}

//判断map中是否包含指定的value    
Map.prototype.containsValue = function (value) {
    try {
        for (var p in this.container) {
            if (this.container[p] === value)
                return true;
        }
        return false;

    } catch (e) {
        return e;
    }
};

//删除map中指定的key    
Map.prototype.remove = function (key) {
    try {
        delete this.container[key];
    } catch (e) {
        return e;
    }
};

//清空map    
Map.prototype.clear = function () {
    try {
        delete this.container;
        this.container = {};

    } catch (e) {
        return e;
    }
};

//判断map是否为空    
Map.prototype.isEmpty = function () {

    if (this.keySet().length == 0)
        return true;
    else
        return false;
};

//获取map的大小    
Map.prototype.size = function () {

    return this.keySet().length;
}

//返回map中的key值数组    
Map.prototype.keySet = function () {
    var keys = new Array();
    for (var p in this.container) {
        keys.push(p);
    }

    return keys;
}

//返回map中的values值数组    
Map.prototype.values = function () {
    var valuesArray = new Array();
    var keys = this.keySet();
    for (var i = 0; i < keys.length; i++) {
        valuesArray.push(this.container[keys[i]]);
    }
    return valuesArray;
}

//返回 map 中的 entrySet 对象
Map.prototype.entrySet = function () {
    var array = new Array();
    var keys = this.keySet();
    for (var i = 0; i < keys.length; i++) {
        array.push(keys[i], this.container[keys[i]]);
    }
    return array;
}

//返回 map 中的 value值的和(当值是 Nunmber 类型时有效)
Map.prototype.sumValues = function () {
    var values = this.values();
    var result = 0;
    for (var i = 0; i < values.length; i++) {
        result += Number(values[i]);
    }
    return result;
}
//==========================HashMap E================================================


//获取url中的参数
function QueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}

//CompareDateDiff("2016-03-28 10:30:22","2016-03-28 10:38:22","minute")
function CompareDateDiff(startTime, endTime, diffType) {
    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式
    startTime = startTime.replace(/\-/g, "/");
    endTime = endTime.replace(/\-/g, "/");
    //将计算间隔类性字符转换为小写
    diffType = diffType.toLowerCase();
    var sTime = new Date(startTime); //开始时间
    var eTime = new Date(endTime); //结束时间
    //作为除数的数字
    var timeType = 1;
    switch (diffType) {
        case"second":
            timeType = 1000;
            break;
        case"minute":
            timeType = 1000 * 60;
            break;
        case"hour":
            timeType = 1000 * 3600;
            break;
        case"day":
            timeType = 1000 * 3600 * 24;
            break;
        default:
            break;
    }
    return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(timeType));
}

//==========================分页 S================================================
function getpage(contactsPage, name) {
    var page = [];
    page.push('<li>');
    if (contactsPage.firstPage) {
        //page.push('<a aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>');
    } else {
        page.push('<a onclick="' + name + '(' + (contactsPage.number - 1) + ')"><span aria-hidden="true">&laquo;</span></a>');
    }
    page.push('</li>');
    for (var i = contactsPage.pageStart; i <= contactsPage.pageEnd; i++) {
        var start = i;
        if (contactsPage.pageStart == contactsPage.pageEnd) {
            break;
        }
        if (contactsPage.number == start) {
            page.push('<li class="active"><a>' + start + '</a></li>');
            continue;
        }
        page.push('<li><a onclick="' + name + '(' + start + ')">' + start + '</a></li>');
    }
    page.push('<li>');
    if (contactsPage.lastPage) {
        //page.push('<a aria-label="Next"><span aria-hidden="true">&raquo;</span></a>');
    } else {
        page.push('<a onclick="' + name + '(' + (contactsPage.number + 1) + ')"><span aria-hidden="true">&raquo;</span></a>');
    }
    page.push('</li>');
    $(".pagination").children().remove();
    $(".pagination").html(page.join(''));
}
//==========================分页 E================================================

//==========================Text S================================================
function isEmpty(obj) {
    if (obj === null || obj === undefined || (typeof obj === "string" && obj.toLowerCase() === "null") || obj === "" || obj === 0 || obj.length === 0) {
        return true;
    } else {
        return false;
    }
}

function replaceStr(str) {
    if (isEmpty(str)) {
        return '--';
    } else {
        return str;
    }
}

/**参数说明：
 * 根据长度截取先使用字符串，超长部分追加…
 * str 对象字符串
 * len 目标字节长度
 * 返回值： 处理结果字符串
 */
function cutString(str, len) {
    //length属性读出来的汉字长度为1
    if (str.length * 2 <= len) {
        return str;
    }
    var strlen = 0;
    var s = "";
    for (var i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 128) {
            strlen += 2;
        } else {
            strlen++;
        }
        s += str.charAt(i);
        if (strlen >= len) {
            return s + "...";
        }
    }
    return s;
}
//==========================Text E================================================


//==========================number S================================================
//使用 var num=number_format(1234567.089, 2, ".", ",");//1,234,567.09
function number_format(number, decimals, dec_point, thousands_sep) {
    /*
     * 参数说明：
     * number：要格式化的数字
     * decimals：保留几位小数
     * dec_point：小数点符号
     * thousands_sep：千分位符号
     * */
    number = (number + '').replace(/[^0-9+-Ee.]/g, '');
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.ceil(n * k) / k;
        };

    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    var re = /(-?\d+)(\d{3})/;
    while (re.test(s[0])) {
        s[0] = s[0].replace(re, "$1" + sep + "$2");
    }

    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}
//==========================number E================================================


