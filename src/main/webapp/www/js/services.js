angular.module('starter.services', ['facebook'])
    .config(function (FacebookProvider) {
        FacebookProvider.init('724763597633401');
    })
    .factory('fbService', ['Facebook', function (Facebook) {
        return {
            login: function () {
                // From now on you can use the Facebook service just as Facebook api says
                Facebook.login(function (response) {
                    // Do something with response.
                });
            },
            me: function () {
                Facebook.api('/me', function (response) {
                    return response;
                });
            }


        }
    }]);
