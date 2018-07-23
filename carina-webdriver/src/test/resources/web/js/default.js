'use strict';

var scope = {};
var hiddenElements = {};

var submitButton = document.getElementById('submitButton');
var responseText = submitButton ? submitButton.attributes['response-text'].value : '';
var statusElement = document.getElementById('status');
var searchInput = document.getElementById('searchInput');
var redSquare = document.getElementById('redSquare');
var graySquare = document.getElementById('graySquare');
var html = document.getElementsByTagName('html')[0];

var cookies = {};
document.cookie.split(';').forEach(function (value) {
    if(value.trim()) {
        cookies[value.split('=')[0]] = value.split('=')[1];
    }
});

var item = document.location.search;
if(item.length) {
    var slices = item.substring(1).split('=');
    try {
        scope[slices[0]] = JSON.parse(decodeURIComponent(slices[1]));
    } catch(e) {
    }
}

var users = [];

hideElementCall(statusElement, 'status');

if(submitButton) {
    submitButton.disabled = true;
    submitButton.onclick = function (ev) {
        ev.preventDefault();
        hideElementCall(statusElement, 'status');
        ajaxCallImitation(2000, function () {
            showElementCall('status');
            ajaxCallImitation(2000, function () {
                hideElementCall(statusElement, 'status');
            });
            statusElement.style.backgroundColor = '#9bf5b1';
            injectIntoStatusElement(responseText);

            // create object
            createUser();
        });
    };
}

if(searchInput) {
    searchInput.oninput = function (ev) {
        var value = searchInput.value;
        var scopeUsers = Object.assign([], scope['users']);
        scope.users = scope.users.filter(function (user) {
            return user.username.indexOf(value) >= 0 || user.email.indexOf(value) >= 0 || user.code.indexOf(value) >= 0 || user.item.indexOf(value) >= 0;
        });
        repeatCall('users');
        scope.users = Object.assign([], scopeUsers);
    };
}

if(redSquare) {
    redSquare.ondblclick = function (ev) {
        graySquare.style.display = graySquare.style.display == 'none' ? 'inline-block' : 'none';
    };
}

if(redSquare && graySquare) {
    var squareParent = redSquare.parentNode.getBoundingClientRect();
    var redSquareRect = redSquare.getBoundingClientRect();
    graySquare.onmousedown = function (clickEvent) {
        graySquare.style.position = 'fixed';
        graySquare.style.left = clickEvent.clientX + 'px';
        html.onmouseover = function (ev) {
            if(ev.clientX > squareParent.x && ev.clientX < squareParent.x + squareParent.width) {
                graySquare.style.left = ev.clientX + 'px';
            }
        };
        html.onmouseup = function (ev) {
            var graySquareRect = graySquare.getBoundingClientRect();
            if(redSquareRect.x + redSquareRect.width >= graySquareRect.x) {
                submitButton.disabled = false;
            }
            graySquare.style.position = 'static';
            html.onmouseover = undefined;
            redSquare.onmouseup = undefined;
            graySquare.onmouseup = undefined;
        };
    };
}

var exportElements = document.querySelectorAll('*[export]');
if(exportElements && exportElements.length) {
    showCall();
    exportElements.forEach(function (value) {
        value.onclick = function (ev) {
            showCall();
        };
    });

}

function generateCode() {
    document.getElementById('codeGenerateInput').value = generateRandomNumber(10000, 100000);
};

function createUser() {
    var user = {username: '', email: '', code: 0, item: ''};
    users.push(exportCall(submitButton, user));
    scope['users'] = users;

    // inject values
    repeatCall('users');
};

function ajaxCallImitation(millis, func) {
    showElementCall('status');
    statusElement.style.backgroundColor = 'rgba(255, 255, 255, 0)';
    injectIntoStatusElement('Loading...');
    setTimeout(function () {
        hideElementCall(statusElement, 'status');
        func.call();
    }, millis);
};

function getFormByElement(element) {
    return element.closest('form');
};

function generateRandomNumber(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
};

// Export directive
function exportCall(formElement, obj) {
    var form = getFormByElement(formElement);
    var inputs = form.querySelectorAll('input:not([type=\'submit\']) , select');
    inputs.forEach(function (input) {
        if(input.attributes.export) {
            var exportValue = input.attributes.export.value;
            if(obj[exportValue] != undefined) {
                obj[exportValue] = input.value;
            }
        }
    });
    return obj;
};

// Show directive
function showCall() {
    var elements = document.querySelectorAll('*[show]');
    elements.forEach(function (value) {
        var triggers = document.querySelectorAll('*[export=\'' + value.attributes.show.value + '\']');
        var show;
        triggers.forEach(function (element) {
            show = element.checked || element.value === 'true';
        });
        if (!show) {
            value.style.display = 'none';
        } else {
            value.style.display = '';
        }
    });
};

var injects = document.querySelectorAll('*[inject-root]');

if(injects && injects.length) {
    injects.forEach(function (value) {
        var injectValue = value.attributes['inject-root'].value;
        if(value) {
            injectCall(value, scope[injectValue]);
        }
    });
}

// Inject directive
function injectCall(rootElement, obj) {
    for(var key in obj) {
        var selector = '*[inject=\'' + key + '\']';
        var elementInjection = rootElement.querySelector(selector);
        if(elementInjection) {
            injectData(elementInjection, obj[key]);
        }
    }
};

function injectData(element, data) {
    element.innerHTML = data;
};

function injectIntoStatusElement(data) {
    injectData(statusElement, '<div style="text-align: center; position: relative; top: 18px;">' + data + '</div>');
};

// Repeat directive
function repeatCall(name) {
    showElementCall(name);
    var elements = document.querySelectorAll('*[repeat=\'' + name + '\']');
    elements.forEach(function (value) {
        var followingSibling = value.nextSibling;
        if(! followingSibling || ! followingSibling.attributes || ! followingSibling.attributes.repeat || followingSibling.attributes.repeat.value !== name) {
            if(scope[value.attributes.repeat.value] && scope[value.attributes.repeat.value].length) {
                repeatElement(value, 0);
            } else {
                hideElementCall(value, value.attributes.repeat.value);
            }
        } else {
            value.remove();
        }
    });
    var importElements = document.querySelectorAll('*[import]');
    if(importElements && importElements.length) {
        importElements.forEach(function (value, index) {
            value.onclick = function (ev) {
                var scopeItemName = value.attributes.import.value;
                var closestData = scope[value.closest('*[repeat]').attributes.repeat.value];
                if(value.href) {
                    value.href = value.href + '?' + scopeItemName + '=' + JSON.stringify(closestData[closestData.length - 1 - index]);
                }
                scope[scopeItemName] = closestData[closestData.length - 1 - index];
            };
        });
    }
};

function repeatElement(element, injectOnlyIndex) {
    var list = scope[element.attributes.repeat.value];
    if(list && list.length) {
        repeatIteration(list, element, injectOnlyIndex);
    }
};

function repeatIteration(objArray, element, injectOnlyIndex) {
    for (var index = 0; index < objArray.length; index++) {
        var item = objArray[index];
        if (index !== injectOnlyIndex) {
            element.insertAdjacentHTML('afterend', element.outerHTML);
        }
        injectCall(element, item);
    }
};

// Hide and show calls
function hideElementCall(element, name) {
    if(isElementPresent(element)) {
        var commentedElement = document.createElement('span');
        var id = name + '-' + generateRandomNumber(0, 10000);
        commentedElement.setAttribute('id', id);
        var hiddenElementArray = hiddenElements[name] && hiddenElements[name].length ? hiddenElements[name] : [];
        hiddenElementArray.push({id: id, el: element});
        hiddenElements[name] = hiddenElementArray;
        commentedElement.style.display = 'none';
        element.parentNode.replaceChild(commentedElement, element);
    }
};

function showElementCall(name) {
    if(hiddenElements[name] && hiddenElements[name].length) {
        hiddenElements[name].forEach(function (el, index) {
            if(el && el.id && el.el && ! isElementPresent(el.el)) {
                var element = document.querySelector('span[id = \'' + el.id + '\']');
                element.parentNode.replaceChild(el.el, element);
                hiddenElements[name][index] = undefined;
            }
        });
    }
};

// Other

function isElementPresent(element) {
    return document.contains(element);
};

function ajaxCallImitation(millis, func) {
    showElementCall('status');
    statusElement.style.backgroundColor = 'rgba(255, 255, 255, 0)';
    injectIntoStatusElement('Loading...');
    setTimeout(function () {
        hideElementCall(statusElement, 'status');
        func.call();
    }, millis);
};

function getFormByElement(element) {
    return element.closest('form');
};

function generateRandomNumber(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
};

// Data

if(cookies.env !== 'testing') {
    var USERS = [
        {
            username: 'username1',
            email: 'email1',
            code: 'code1',
            item: 'item1'
        },
        {
            username: 'username2',
            email: 'email2',
            code: 'code2',
            item: 'item2'
        },
        {
            username: 'username3',
            email: 'email3',
            code: 'code3',
            item: 'item3'
        },
        {
            username: 'username4',
            email: 'email4',
            code: 'code4',
            item: 'item4'
        },
        {
            username: 'username5',
            email: 'email5',
            code: 'code5',
            item: 'item5'
        },
        {
            username: 'username6',
            email: 'email6',
            code: 'code6',
            item: 'item6'
        },
        {
            username: 'username7',
            email: 'email7',
            code: 'code7',
            item: 'item7'
        },
        {
            username: 'username8',
            email: 'email8',
            code: 'code8',
            item: 'item8'
        },
        {
            username: 'username9',
            email: 'email9',
            code: 'code9',
            item: 'item9'
        },
        {
            username: 'username10',
            email: 'email10',
            code: 'code10',
            item: 'item10'
        },
        {
            username: 'username11',
            email: 'email11',
            code: 'code11',
            item: 'item11'
        },
        {
            username: 'username12',
            email: 'email12',
            code: 'code12',
            item: 'item12'
        },
        {
            username: 'username13',
            email: 'email13',
            code: 'code13',
            item: 'item13'
        },
        {
            username: 'username14',
            email: 'email14',
            code: 'code14',
            item: 'item14'
        },
        {
            username: 'username15',
            email: 'email15',
            code: 'code15',
            item: 'item15'
        },
        {
            username: 'username16',
            email: 'email16',
            code: 'code16',
            item: 'item16'
        },
        {
            username: 'username17',
            email: 'email17',
            code: 'code17',
            item: 'item17'
        },

    ];

    scope['users'] = USERS;
}

repeatCall('users');