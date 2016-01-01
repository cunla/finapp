appCtrllers.controller('loginCtrl', ['$scope', '$state', 'FinApp',
    function ($scope, $state, FinApp) {

        $scope.user = {};
        $scope.fbLogin = function () {
            FinApp.fbLogin().then(function (user) {
                if (user.lastGroupId) {
                    $state.go('home', {"groupId": user.lastGroupId});
                } else {
                    $state.go('profile');
                }
            })
        };
        $scope.login = function (user) {
            FinApp.login(user.email, user.password)
                .success(function (res) {
                    var user = res.data;
                    if (user.lastGroupId) {
                        $state.go('home', {"groupId": user.lastGroupId});
                    } else {
                        $state.go('profile');
                    }
                }).error(function (err) {

                }
            );
        }

    }]);
