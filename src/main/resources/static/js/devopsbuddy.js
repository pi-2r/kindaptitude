$( document ).ready( main );

function main() {

    $('.btn-collapse').click(function(e) {
        e.preventDefault();
        var $this = $(this);
        var $collapse = $this.closest('.collapse-group').find('.collapse');
        $collapse.collapse('toggle');
    });

    /* Contact form validation */
    $('#contactForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            email: {
                validators: {
                    notEmpty: {
                        message: 'The email is required'
                    },
                    emailAddress: {
                        message: 'The input is not a valid email address'
                    }
                }
            },
            firstName: {
                validators: {
                    notEmpty: {
                        message: 'The first name is required'
                    }
                }
            },
            lastName: {
                validators: {
                    notEmpty: {
                        message: 'The last name is required'
                    }
                }
            },
            feedback: {
                validators: {
                    notEmpty: {
                        message: 'Your feedback is valued and required'
                    }
                }
            }
        }
    });

    /* Forgot password form validation */
    $('#forgotPasswordForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            email: {
                validators: {
                    notEmpty: {
                        message: 'The email is required'
                    },
                    emailAddress: {
                        message: 'The input is not a valid email address'
                    }
                }
            }
        }
    });

    $('#savePasswordForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            password: {
                validators: {
                    notEmpty: {
                        message: 'The password is required'
                    },
                    identical: {
                        field: 'confirmPassword',
                        message: 'The password and its confirmation are not the same'
                    }
                }
            },
            confirmPassword: {
                validators: {
                    notEmpty: {
                        message: 'The confirmation password is required'
                    },
                    identical: {
                        field: 'password',
                        message: 'The password and its confirmation are not the same'
                    }
                }
            }
        }
    });

    /* Login page form validation */
    $('#loginForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                validators: {
                notEmpty: {
                    message: 'The email is required'
                },
                emailAddress: {
                    message: 'The input is not a valid email address'
                }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: 'The password is required'
                    }
                }
            }
        }
    });

    /* Signup form validation */
    $('#signupForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            email: {
                validators: {
                    notEmpty: {
                        message: 'The email is required'
                    },
                    emailAddress: {
                        message: 'The input is not a valid email address'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: 'The password is required'
                    },
                    identical: {
                        field: 'confirmPassword',
                        message: 'The password and its confirm are not the same'
                    }
                }
            },
            confirmPassword: {
                validators: {
                    notEmpty: {
                        message: 'The confirmation password is required'
                    },
                    identical: {
                        field: 'password',
                        message: 'The password and its confirm are not the same'
                    }
                }
            },
            firstName: {
                validators: {
                    notEmpty: {
                        message: 'The first name is required'
                    }
                }
            },
            lastName: {
                validators: {
                    notEmpty: {
                        message: 'The last name is required'
                    }
                }
            },
            country: {
                validators: {
                    notEmpty: {
                        message: 'The country is required'
                    }
                }
            },
            birthday: {
                validators: {
                    notEmpty: {
                        message: 'The date is required'
                    },
                    date: {
                        format: 'MM/DD/YYYY',
                        message: 'The date is not a valid'
                    }
                }
            },
            agree: {
                validators: {
                    notEmpty: {
                        message: 'You must agree with the terms and conditions'
                    }
                }
            },
            captcha: {
                validators: {
                    notEmpty: {
                        message: 'The captcha is required'
                    }
                }
            }
        }
    });


    /* changeProfile form validation */
    $('#changeProfile').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            firstName: {
                validators: {
                    notEmpty: {
                        message: 'Le prénom est requis'
                    }
                }
            },
            lastName: {
                validators: {
                    notEmpty: {
                        message: 'Le nom est requis'
                    }
                }
            },
            dateOfBirth: {
                validators: {
                    notEmpty: {
                        format: '/DD/MM/YYYY',
                        message: 'La date n\'est pas valide'
                    }
                }
            },
            phoneNumber: {
                validators: {
                    notEmpty: {
                        message: 'Le champs ne peut pas etre vide'
                    },
                    phone: {
                        country: 'countrySelectBoxProfil',
                        message: 'The value is not valid %s phone number'
                    }
                }
            },
            countrySelectBoxProfil: {
                validators: {
                    notEmpty: {
                        message: 'Le pays est requis'
                    }
                }
            }
        }
    });

    /* changeProfile form validation */
    $('#changeBusiness').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            nameOfComapny: {
                validators: {
                    notEmpty: {
                        message: 'Le nom de votre entreprise est requis'
                    }
                }
            },
            kindOfOrganisation: {
                validators: {
                    notEmpty: {
                        message: 'Le type d\'organisation est requis'
                    }
                }
            },
            sirenOfCompany: {
                validators: {
                    callback: {
                        callback: function (value, validator, $field) {
                            // Determine if the input EPRID already exists
                            var siren = $('#sirenOfCompany').val();
                            checkSiren = verifiersiren(siren);
                            if (checkSiren == true)
                                return true;
                            else
                                return {
                                    valid: false,
                                    message: 'Le siren n\'est pas valide'
                                };


                        }
                    }
                }
            },
            tva: {
                validators: {
                    notEmpty: {
                        message: 'La TVA est requis'
                    }
                }
            },
            street: {
                validators: {
                    notEmpty: {
                        message: 'La rue ne peut etre vide'
                    }
                }
            },
            zipCode: {
                validators: {
                    notEmpty: {
                        message: 'le code postale ne peut etre vide'
                    }
                }
            },
            town: {
                validators: {
                    notEmpty: {
                        message: 'la ville ne peut etre vide'
                    }
                }
            },
            emailContact: {
                validators: {
                    notEmpty: {
                        message: 'l \'email de référence ne peut etre vide'
                    }
                }
            },
            phoneNumberContact: {
                validators: {
                    notEmpty: {
                        message: 'Le numéro de téléphone de référence ne peut etre vide'
                    }
                }
            }

        }
    });

    /* changeProfile form validation */
    $('#addUser').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            email: {
                validators: {
                    notEmpty: {
                        message: 'L\'adresse email est requise'
                    },
                    emailAddress: {
                        message: 'Cet email n\'est pas valide'
                    }
                }
            },
            firstName: {
                validators: {
                    notEmpty: {
                        message: 'Le prénom est requis'
                    }
                }
            },
            lastName: {
                validators: {
                    notEmpty: {
                        message: 'Le nom est requis'
                    }
                }
            }
        }
    });

    /* selectYourChoise form validation */
    $('#selectYourChoise').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            nbrSubscription: {
                validators: {
                    notEmpty: {
                        message: 'Le prénom est requis'
                    }
                }
            },
            lastName: {
                validators: {
                    notEmpty: {
                        message: 'Le nom est requis'
                    }
                }
            }
        }
    });


    $('#managecb').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            ccNumber: {
                selector: '[data-stripe="number"]',
                validators: {
                    notEmpty: {
                        message: 'The credit card number is required'
                    },
                    creditCard: {
                        message: 'The credit card number is not valid'
                    }
                }
            },
            expMonth: {
                selector: '[data-stripe="exp-month"]',
                row: '.col-xs-3',
                validators: {
                    notEmpty: {
                        message: 'The expiration month is required'
                    },
                    digits: {
                        message: 'The expiration month can contain digits only'
                    },
                    callback: {
                        message: 'Expired',
                        callback: function(value, validator) {
                            value = parseInt(value, 10);
                            var year         = validator.getFieldElements('expYear').val(),
                                currentMonth = new Date().getMonth() + 1,
                                currentYear  = new Date().getFullYear();
                            if (value < 0 || value > 12) {
                                return false;
                            }
                            if (year == '') {
                                return true;
                            }
                            year = parseInt(year, 10);
                            if (year > currentYear || (year == currentYear && value >= currentMonth)) {
                                validator.updateStatus('expYear', 'VALID');
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            },
            expYear: {
                selector: '[data-stripe="exp-year"]',
                row: '.col-xs-3',
                validators: {
                    notEmpty: {
                        message: 'The expiration year is required'
                    },
                    digits: {
                        message: 'The expiration year can contain digits only'
                    },
                    callback: {
                        message: 'Expired',
                        callback: function(value, validator) {
                            value = parseInt(value, 10);
                            var month        = validator.getFieldElements('expMonth').val(),
                                currentMonth = new Date().getMonth() + 1,
                                currentYear  = new Date().getFullYear();
                            if (value < currentYear || value > currentYear + 100) {
                                return false;
                            }
                            if (month == '') {
                                return false;
                            }
                            month = parseInt(month, 10);
                            if (value > currentYear || (value == currentYear && month >= currentMonth)) {
                                validator.updateStatus('expMonth', 'VALID');
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            },
            cvvNumber: {
                selector: '[data-stripe="cvc"]',
                validators: {
                    notEmpty: {
                        message: 'The CVV number is required'
                    },
                    cvv: {
                        message: 'The value is not a valid CVV',
                        creditCardField: 'ccNumber'
                    }
                }
            }
        }
    });

}