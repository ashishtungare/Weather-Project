<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<jsp:include page="layout/header.jsp" />

<div class="jumbotron">
    <p>
        This application uses Accuweather API to get location and temperature information</P>
    </p>
    <br />
    <form action="" method="post" id="formular" class="form-horizontal">
        <div class="form-group">
            <label class="col-md-3 control-label">Enter Zip Code:</label>
            <div class="col-md-3">
                <input type="text" name="zcode" id="zcode" class="form-control" />
            </div>
            <div class="col-md-1">
                <input type="submit" class="btn btn-md btn-primary" />
            </div>
        </div>
    </form>
</div>
	
<script>
    $(document).ready(function() {
        $("#formular").submit(function(event) {
            var zc = $("#zcode").val();
            if(zc.length !== 5){
                alert('Invalid zip code format !');
                return;
            }
            var isnum = /^\d+$/.test(zc);
            if(!isnum){
                alert('Invalid zip code format !');
                return;
            }
            event.preventDefault();
            $.ajax({
                type: 'get',
                url: 'locationinfo?zipcode=' + zc,
                beforeSend: function () {
                    document.body.style.cursor = 'wait';
                    $("#result").html('<p>' + " " + '</p>');
                },
                success: function (data) {
                    document.body.style.cursor = 'pointer';
                    if(data === ""){
                        $("#result").html('<p>Zip code not found !</p>');                      
                    }else{
                        var obj = JSON.parse(data);
                        var outString = "Temperature of " + obj.CITY + ", " +
                            obj.STATE + ", " + obj.COUNTRY + " is " +
                            obj.TEMPERATURE;
                        $("#result").html('<p>' + outString + '</p>');
                    }
                },
                error: function () {
                    document.body.style.cursor = 'pointer';
                    $("#result").html('<p>Zip code not found !</p>');
                }
            });
        });
    });
</script>

<div id="result" class="jumbotron">
</div>

<jsp:include page="layout/footer.jsp" />