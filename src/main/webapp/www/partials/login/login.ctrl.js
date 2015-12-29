appCtrllers.controller('loginCtrl', ['$scope', '$state', '$cookies', 'FinApp',
    function ($scope, $state, $cookies, FinApp) {

        $scope.user = {};
        $scope.fbLogin = function () {
            FinApp.fbLogin().then(function (user) {
                $state.go('login');
            })
        };
        $scope.login = function (user) {
            FinApp.login(user.email, user.password)
                .success(function (user) {
                    $state.go('login');
                }).error(function (err) {

                }
            );
        }

    }]);
