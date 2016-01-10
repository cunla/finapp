(function () {
    angular.module('app.controllers')
        .controller('loginCtrl', ['$scope', '$state', 'fin', loginCtrl]);
    function loginCtrl($scope, $state, fin) {
        function nextState(user) {
            if (user.lastGroupId) {
                $state.go('menu.home', {"groupId": user.lastGroupId});
            } else {
                $state.go('menu.profile');
            }
        }

        $scope.user = {};
        $scope.fbLogin = function () {
            fin.fbLogin().then(function (user) {
                nextState(user);
            })
        };
        $scope.login = function (user) {
            fin.login(user.email, user.password)
                .success(function (res) {
                    var user = res.data;
                    nextState(user);
                }).error(function (err) {

                }
            );
        }

    }
})();
