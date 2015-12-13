appCtrllers.controller('profileCtrl', function ($scope, $window, $state, $cookies, FinApp) {
    $scope.user = $cookies.getObject('userInfo');

    $scope.refresh = function () {
        FinApp.currentUser().then(function (res) {
            $scope.user = res.data;
            FinApp.getGroups().then(function (res) {
                $scope.groups = res.data;
                for (group in $scope.groups) {
                    $scope.groups[group].isAdmin = ($scope.groups[group].admin.id == $scope.user.id);
                }
            });
        });
    };

    $scope.createGroup = function (groupName) {
        var group = {"name": groupName, "label": groupName};
        FinApp.createGroup(group).then(function (res) {
            $scope.newGroupForm = false;
            $scope.refresh();
        });
    };

    $scope.logout = function () {
        $cookies.remove("userInfo");
        FinApp.logout();
        $state.go('login');
        $window.location.reload();
    };
    $scope.refresh();
});
