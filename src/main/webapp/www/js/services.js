var basicUrl='/finapp';
app.factory('FinApp',['$http','$q',function($http,$q){
    return {
        fbLogin:function(){
            return $q(function(resolve,reject){
                FB.login(function (response) {
                    if (response.authResponse) {
                        getUserInfo();
                    } else {
                        console.log('User cancelled login or did not fully authorize.');
                        reject('User cancelled login or did not fully authorize.');
                    }
                }, {scope: 'email'});

                function getUserInfo() {
                    // get basic info
                    FB.api('/me',
                        {fields: "id,about,age_range,picture,bio,birthday,context,email,first_name,gender,hometown,link,location,middle_name,name,timezone,website,work"},
                        function (response) {
                        console.log('Facebook Login RESPONSE: ' + angular.toJson(response));
                        // get profile picture
                        FB.api('/me/picture?type=normal', function (picResponse) {
                            console.log('Facebook Login RESPONSE: ' + picResponse.data.url);
                            response.imageUrl = picResponse.data.url;
                            // After posting user data to server successfully store user data locally
                            var user = {};
                            user.name = response.name;
                            user.email = response.email;
                            if (response.gender) {
                                response.gender.toString().toLowerCase() === 'male' ? user.gender = 'M' : user.gender = 'F';
                            } else {
                                user.gender = '';
                            }
                            user.profilePic = picResponse.data.url;
                            $http.post(basicUrl+'/facebookuser?accountId='+response.id,user,{headers :{"Content-Type":"application/json"}})
                            .then(function(data){
                                console.log('FinApp fbLogin response: '+angular.toJson(data));
                                resolve(user);
                            })
                        });
                    });
                }
            });
        },
        logout:function(){
            return $http.post(basicUrl+'/logout-action');
        }
    };
}]);

