var basicUrl = '/finapp';
app
    .factory('FinApp', ['$http', '$q', function ($http, $q) {
        return {
            login: function (email, password) {
                var data = 'username=' + email + '&password=' + password;
                return $http({
                    method: "post",
                    url: basicUrl + '/login-action',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    data: data
                });
            },
            register: function (email, password) {
                return $http({
                    method: "post",
                    url: basicUrl + '/users',
                    headers: {'Content-Type': 'application/json'},
                    data: {email: email, password: password}
                });
            },
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
                                            resolve(data.data);
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
            },
            getTransactions: function (group) {
                return $http.get(basicUrl + '/groups/' + group + '/transactions');
            },
            getTransaction: function (tId) {
                return $http.get(basicUrl + '/transactions/' + tId);
            },
            deleteTransaction: function (tId) {
                return $http.delete(basicUrl + '/transactions/' + tId);
            },
            updateTransaction: function (tId, transaction) {
                return $http.put(basicUrl + '/transactions/' + tId, transaction);
            },
            newTransaction: function (group, amount) {
                return $q(function (resolve, reject) {
                    var transaction = {};
                    transaction.group = group;
                    transaction.amount = amount;
                    transaction.date = new Date();
                    navigator.geolocation.getCurrentPosition(function (pos) {
                        var point = {latitude: pos.coords.latitude, longitude: pos.coords.longitude};
                        transaction.location = point;
                        $http.post(basicUrl + "/groups/" + group + "/transactions", transaction).then(function (res) {
                            resolve(res.data);
                        })
                    }, function (error) {
                        reject('Unable to get location: ' + error.message);
                    });
                });
            }
        };
    }]);

