var basicUrl='/finapp';
app.factory('FinApp',['$http',function($http){
    return {
        login:function(user,password){
            var body="username="+user+"&password="+password;
            return $http.post(basicUrl+'/login-action',body,{headers :{"Content-Type":"application/x-www-form-urlencoded"}});
        },
        logout:function(){
            return $http.post(basicUrl+'/logout-action');
        }
    };
}]);

