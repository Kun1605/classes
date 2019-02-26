UE.plugins['history'] = function () {

    function btnEvents() {
        //fullscreen
        var isFullScreen = false ;
        $('#edui1001').on('click', function () {
            $('#ue_fullscreen').trigger("click");
            if (!isFullScreen){//open full
                $('#edui1_iframeholder').css({"width":"900"});
                isFullScreen = true;

                $("#edui1").css({"background-color":"#dedede"});
                $("#ueditor_0").contents().find("body").css({"background-color":"#ffffff"});
            }else {//exit full
                $('#edui1_iframeholder').css({"width":"100%"});
                isFullScreen = false;
            }
        });
    }

};
