(function () {
    var basicUrl = '/finapp';

    angular.module('app')
        .factory('commons', function () {
            return {
                datePickerObject: datePickerFunc,
                beginningOfMonth: beginningOfMonth,
                endOfMonth: endOfMonth,
                colors: colors,
                icons: icons,
                accountIcons: accountIcons
            }
        })
        .factory('fin', ['$http', '$q', function ($http, $q) {
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
                fbLogin: fbLogin,
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
                getTransactions: getTransactions,
                validateAccount: validateAccount,
                getTransaction: function (tId) {
                    return $http.get(basicUrl + '/transactions/' + tId);
                },
                deleteTransaction: function (tId) {
                    return $http.delete(basicUrl + '/transactions/' + tId);
                },
                updateTransaction: function (tId, transaction) {
                    return $http.put(basicUrl + '/transactions/' + tId, transaction);
                },
                newTransaction: newTransaction,
                getCategoryReport: function (group, start, end) {
                    var period = {"start": start, "end": end};
                    return $http.put(basicUrl + "/groups/" + group + "/categories", period);
                },
                getAccountsReport: function (group) {
                    return $http.get(basicUrl + "/groups/" + group + "/accounts");
                },
                createCategory: function (group, cat) {
                    return $http.post(basicUrl + "/groups/" + group + "/categories", cat);
                },
                createAccount: function (group, acc) {
                    return $http.post(basicUrl + "/groups/" + group + "/accounts", acc);
                }
            };

            function validateAccount(group, accountId) {
                var url = basicUrl + "/groups/" + group + "/accounts/" + accountId+"/validate";
                return $http.get(url);
            }

            function getTransactions(group, page, size) {
                var url = basicUrl + '/groups/' + group + '/transactions';
                if (page && size) {
                    url = url + "?page=" + page + "&pageSize=" + size;
                }
                return $http.get(url);
            }

            function newTransaction(group, amount) {
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

            function fbLogin() {
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
            }
        }]);

    function datePickerFunc(title, date) {
        var d = {
            titleLabel: title,  //Optional
            todayLabel: 'Today',  //Optional
            closeLabel: 'Close',  //Optional
            setLabel: 'Set',  //Optional
            setButtonType: 'button-assertive',  //Optional
            todayButtonType: 'button-assertive',  //Optional
            closeButtonType: 'button-assertive',  //Optional
            inputDate: date,  //Optional
            mondayFirst: false,  //Optional
            //disabledDates: disabledDates, //Optional
            //weekDaysList: weekDaysList, //Optional
            //monthList: monthList, //Optional
            templateType: 'popup', //Optional
            showTodayButton: 'true', //Optional
            modalHeaderColor: 'bar-positive', //Optional
            modalFooterColor: 'bar-positive', //Optional
            //from: new Date(2012, 8, 2), //Optional
            //to: new Date(2018, 8, 25),  //Optional
            callback: function (val) {  //Mandatory
                d.inputDate = (val);
            },
            dateFormat: 'dd-MM-yyyy', //Optional
            closeOnSelect: false //Optional
        };
        return d;
    }

    function beginningOfMonth(month) {
        var date = new Date();
        if (month) {
            date.setMonth(month);
        }
        return new Date(date.getFullYear(), date.getMonth(), 1);
    }

    function endOfMonth(month) {
        var date = new Date();
        if (month) {
            date.setMonth(month);
        }
        return new Date(date.getFullYear(), date.getMonth() + 1, 0);
    }

    function colors() {
        return ['stable', 'positive', 'calm', 'balanced', 'energized', 'assertive', 'royal', 'dark'];
    };
    function icons() {
        return ['ion-bag',
            'ion-ios-cart',
            'ion-home',
            'ion-earth',
            'ion-model-s',
            'ion-card',
            'ion-social-bitcoin-outline',
            'ion-cash',
            'ion-social-usd',
            'ion-social-facebook',
            'ion-social-euro',
            'ion-social-tux',
            'ion-social-apple',
            'ion-social-android',
            'ion-record',
            'ion-umbrella',
            'ion-plane', 'ion-pie-graph', 'ion-beer', 'ion-coffee', 'ion-tshirt-outline', 'ion-icecream',
            'ion-wineglass', 'ion-woman', 'ion-man', 'ion-ios-briefcase-outline', 'ion-ios-medkit-outline'

        ];
    };
    function accountIcons() {
        return ['ion-bag',
            'ion-home',
            'ion-card',
            'ion-cash',
            'ion-social-usd',
            'ion-social-euro',
            'ion-briefcase'
        ];
    }
})();

