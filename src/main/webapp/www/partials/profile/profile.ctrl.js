(function () {
    angular.module('app.controllers')
        .controller('profileCtrl', ['$rootScope', '$scope', '$window', '$state', 'FinApp', '$ionicPopup', profileCtrl]);
    function profileCtrl($rootScope, $scope, $window, $state, FinApp, $ionicPopup) {
        //$scope.user = $cookies.getObject('userInfo');

        $scope.refresh = function () {
            FinApp.getGroups().then(function (res) {
                $scope.groups = res.data;
                for (group in $scope.groups) {
                    $scope.groups[group].isAdmin = ($scope.groups[group].admin.id == $rootScope.user.id);
                }

            }).finally(function () {
                $scope.$broadcast('scroll.refreshComplete');
            });
        };

        $scope.createGroup = function (groupName) {
            if (groupName && groupName != "") {
                var group = {"name": groupName, "label": groupName};
                FinApp.createGroup(group).then(function (res) {
                    $scope.newGroupForm = false;
                    $scope.refresh();
                });
            } else {
                var confirmPopup = $ionicPopup.alert({
                    title: 'Input error',
                    template: "You can't create a group with empty name"
                });
                confirmPopup.then(function (res) {
                    console.log(res);
                })
            }
        };
        $scope.setNewGroupFormStatus = function (stat) {
            $scope.newGroupForm = stat;
        }

        $scope.updateUser = function (user) {
            FinApp.updateUser(user).then(function () {
            });
        };

        $scope.updateGender = function (user, gender) {
            user.gender === gender;
            $scope.updateUser(user);
        };

        $scope.logout = function () {
            //$cookies.remove("userInfo");
            FinApp.logout();
            $state.go('login');
            $window.location.reload();
        };

        $scope.refresh();
    }
})();
