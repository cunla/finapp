appCtrllers.controller('loginCtrl', ['$scope', '$state', '$cookies','FinApp',
function ($scope, $state, $cookies, FinApp) {

    $scope.fbLogin = function () {
        FB.login(function (response) {
            if (response.authResponse) {
                getUserInfo();
            } else {
                console.log('User cancelled login or did not fully authorize.');
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
                    FinApp.login(response.email,response.id);
                    // Todo
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
                    $cookies.putObject('userInfo', user);
                    $state.go('profile');

                });
            });
        }
    };
    $scope.gplusLogin = function () {
        var myParams = {
            // Replace client id with yours
            'clientid': '18301237550-3vlqoed2en4lvq6uuhh88o2h1l9m70tr.apps.googleusercontent.com',
            'cookiepolicy': 'single_host_origin',
            'callback': loginCallback,
            'approvalprompt': 'force',
            'scope': 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
        };
        gapi.auth.signIn(myParams);

        function loginCallback(result) {
            if (result['status']['signed_in']) {
                var request = gapi.client.plus.people.get({'userId': 'me'});
                request.execute(function (resp) {
                    console.log('Google+ Login RESPONSE: ' + angular.toJson(resp));
                    var userEmail;
                    if (resp['emails']) {
                        for (var i = 0; i < resp['emails'].length; i++) {
                            if (resp['emails'][i]['type'] == 'account') {
                                userEmail = resp['emails'][i]['value'];
                            }
                        }
                    }
                    // store data to DB
                    var user = {};
                    user.name = resp.displayName;
                    user.email = userEmail;
                    if (resp.gender) {
                        resp.gender.toString().toLowerCase() === 'male' ? user.gender = 'M' : user.gender = 'F';
                    } else {
                        user.gender = '';
                    }
                    user.profilePic = resp.image.url;
                    $cookies.putObject('userInfo', user);
                    $state.go('profile');
                });
            }
        }
    };

}]);
