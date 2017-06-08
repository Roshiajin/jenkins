UserService = function() {
    "use strict";

    var self = this;

    this.loadUsers = function() {
        var table = document.getElementById('users_table');
        table.innerHTML = "<tr> <td>id</td> <td>firstName</td> <td>lastName</td> <td>login</td> <td>email</td> </tr>";
        var xhr = new XMLHttpRequest();
        xhr.open(
            'GET',
            '/services/user'
        );
        xhr.onload = function () {
            if (xhr.status === 200) {
                var users = JSON.parse(xhr.responseText);

                for (var i = 0; i < users.length; i++) {
                    var rowCount = table.getElementsByTagName("tr").length;
                    var row = table.insertRow(rowCount);

                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);

                    cell1.innerHTML = users[i].id;
                    cell2.innerHTML = users[i].firstName;
                    cell3.innerHTML = users[i].lastName;
                    cell4.innerHTML = users[i].login;
                    cell5.innerHTML = users[i].email;
                }
            }
        };
        xhr.send();
    };

    this.addUser = function() {
        var firstName = window.prompt("Enter User firstName:");
        var lastName = window.prompt("Enter user lastName:");
        var login = window.prompt("Enter user login:");
        var email = window.prompt("Enter user email:");

        if (firstName != null && lastName != null && login != null && email != null) {

                var user = {
                    'id':0,
                    'firstName': firstName,
                    'lastName': lastName,
                    'login': login,
                    'email': email
                };

                var xhr = new XMLHttpRequest();
                xhr.open(
                    'POST',
                    '/services/user'
                );
                xhr.setRequestHeader('Content-Type', 'application/json');
                xhr.onload = function () {
                    if (xhr.status === 201) {
                        self.loadUsers();
                    } else {
                        window.alert(xhr.responseText);
                    }
                };
                xhr.send(JSON.stringify(user));

        }
    };

    this.removeUser = function() {
        var id = window.prompt("Enter User id:");
        if (id != null) {
            var xhr = new XMLHttpRequest();
            xhr.open(
                'DELETE',
                '/services/user/' + id
            );
            xhr.onload = function () {
                if (xhr.status === 200) {
                    self.loadUsers();
                } else {
                    window.alert(xhr.responseText);
                }
            };
            xhr.send();
        }
    }
};