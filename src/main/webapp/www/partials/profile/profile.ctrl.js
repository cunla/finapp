appCtrllers.controller('profileCtrl',
    function ($rootScope, $scope, $window, $state, $location, FinApp) {
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
        var group = {"name": groupName, "label": groupName};
        FinApp.createGroup(group).then(function (res) {
            $scope.newGroupForm = false;
            $scope.refresh();
        });
    };
    $scope.showNewGroup = function () {
        $scope.newGroupForm = true;
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
        $state.go('menu.login');
        $window.location.reload();
    };

    $scope.refresh();
});
