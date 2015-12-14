var basicUrl = '/finapp';
app
    .factory('Places', ['$http', function ($http) {
        var baseUrl = "maps.googleapis.com/maps/api/place/nearbysearch/json?"
            + "radius=50&sensor=true";
        return {
            findPlaces: function (lat, lng) {
                var url = baseUrl;
                url += "&location=" + lat + "," + lng;
                url += "&key=AIzaSyBcJ63yGXQar1AKavg7gVwKhKLabdf8HCE";
                return $http.get(url);
            }
        }
    }])
    .factory('FinApp', ['$http', '$q', function ($http, $q) {
        return {
            fbLogin: function () {
                return $q(function (resolve, reject) {
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
                                    user.avatar = picResponse.data.url;
                                    $http.post(basicUrl + '/facebookuser?accountId=' + response.id, user, {headers: {"Content-Type": "application/json"}})
                                        .then(function (data) {
                                            resolve(user);
                                        })
                                });
                            });
                    }
                });
            },
            logout: function () {
                return $http.post(basicUrl + '/logout-action');
            },
            createGroup: function (group) {
                return $http.post(basicUrl + '/groups', group);
            },
            getGroups: function () {
                return $http.get(basicUrl + '/groups');
            },
            currentUser: function () {
                return $http.get(basicUrl + '/users/current-user');
            },
            updateUser: function (user) {
                return $http.post(basicUrl + '/users/' + user.id, user);
            }
        };
    }]);

