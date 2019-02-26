define(['jquery', 'bootstrap', 'backend', 'addtabs', 'table', 'echarts', 'echarts-theme'], function ($, undefined, Backend, Datatable, Table, Echarts) {
    $(function(){
        $.post('/admin/click', {}, function (data) {
            console.log(data);
            BanJiData = data;
            Controller.index();
        }, 'json');
    })

    var Controller = {
        index: function () {
            // 基于准备好的dom，初始化echarts实例
            var myChart = Echarts.init(document.getElementById('echart'), 'walden');
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '仅统计一个月内对班集访问',
                    subtext: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['访问网站次数', '访问IP数']
                },
                toolbox: {
                    show: false,
                    feature: {
                        magicType: {show: true, type: ['stack', 'tiled']},
                        saveAsImage: {show: true}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: true,
                    data: BanJiData.column
                },
                yAxis: {

                },
                grid: [{
                        left: 'left',
                        top: 'top',
                        right: '10',
                        bottom: 30
                    }],
                series: [{
                        name: '访问IP数',
                        type: 'line',
                        smooth: true,
                        areaStyle: {
                            normal: {
                            }
                        },
                        lineStyle: {
                            normal: {
                                width: 1.5
                            }
                        },
                        data: BanJiData.paydata
                    },
                    {
                        name: '访问网站次数',
                        type: 'line',
                        smooth: true,
                        areaStyle: {
                            normal: {
                            }
                        },
                        lineStyle: {
                            normal: {
                                width: 1.5
                            }
                        },
                        data: BanJiData.createdata
                    }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);

            //动态添加数据，可以通过Ajax获取数据然后填充
//            setInterval(function () {
//                BanJiData.column.push((new Date()).toLocaleTimeString().replace(/^\D*/, ''));
//                var amount = Math.floor(Math.random() * 200) + 20;
//                BanJiData.createdata.push(amount);
//                BanJiData.paydata.push(Math.floor(Math.random() * amount) + 1);
//
//                //按自己需求可以取消这个限制
//                if (BanJiData.column.length >= 20) {
//                    //移除最开始的一条数据
//                    BanJiData.column.shift();
//                    BanJiData.paydata.shift();
//                    BanJiData.createdata.shift();
//                }
//                myChart.setOption({
//                    xAxis: {
//                        data: BanJiData.column
//                    },
//                    series: [{
//                            name: '访问IP数',
//                            data: BanJiData.paydata
//                        },
//                        {
//                            name: '访问网站次数',
//                            data: BanJiData.createdata
//                        }]
//                });
//            }, 2000);
            $(window).resize(function () {
                myChart.resize();
            });
        }
    };

    return Controller;
});