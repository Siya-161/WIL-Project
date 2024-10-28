//Initialize Swiper
document.addEventListener("DOMContentLoaded", function() {
    var swiper = new Swiper('.swiper-container', {
        loop: true,
        speed: 1000,
        grabCursor: true,
        watchSlidesProgress: true,
        mousewheelControl: true,
        keyboardControl: true,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        pagination: {
            el: '.swiper-pagination',
        },
        autoplay: {
            delay: 5000, // Adjust delay between slides
            disableOnInteraction: false, // Enable interaction with slides during autoplay
        },
        on: {
            init: function () {
                // Start the animation for the initial slide
                var currentSlide = this.slides[this.activeIndex];
                var slideContent = currentSlide.querySelector('.slide-content');
                setTimeout(function () {
                    slideContent.classList.add('show');
                }, 300); // Adjust the delay as needed
            },
            slideChangeTransitionStart: function () {
                var currentSlide = this.slides[this.activeIndex];
                var slideContent = currentSlide.querySelector('.slide-content');
                slideContent.classList.remove('show');
            },
            slideChangeTransitionEnd: function () {
                var currentSlide = this.slides[this.activeIndex];
                var slideContent = currentSlide.querySelector('.slide-content');
                setTimeout(function () {
                    slideContent.classList.add('show');
                }, 300); // Adjust the delay as needed
            }
        }
    });
});

// Function to scroll to top when back to top button is clicked
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

// Show back to top button when scrolling down
window.addEventListener("scroll", function() {
    var button = document.getElementById("back-to-top");
    if (window.pageYOffset > 900) { // Adjust this value as needed
        button.style.display = "block";
    } else {
        button.style.display = "none";
    }
});

// Update copyright info
var theDate = new Date();
var copyrightInfo = document.getElementById("copyright-info");
copyrightInfo.textContent = "\u00A9 " + theDate.getFullYear() + " Liteboho Solutions. All rights reserved.";

//Function for the accordion
document.addEventListener("DOMContentLoaded", function() {
    var accordionHeaders = document.querySelectorAll('.accordion-header');

    accordionHeaders.forEach(function(header) {
        header.addEventListener('click', function() {

            this.classList.toggle('active');

            var content = this.nextElementSibling;

            if (content.style.display === 'block' && !this.classList.toggle('active')) {
                return;
            } else {

                content.style.display = 'block';
                var allContents = document.querySelectorAll('.accordion-content');
                allContents.forEach(function(item) {
                    if (item !== content && item.style.display === 'block') {
                        item.style.display = 'none';
                        item.previousElementSibling.classList.remove('active');
                    }
                });
            }
        });
    });
});

// Email error and suggestion function
document.addEventListener('DOMContentLoaded', function () {
    const emailInput = document.getElementById('email');
    const emailError = document.getElementById('emailError');
    const emailSuggestion = document.getElementById('emailSuggestion');
    const emailDomains = ['gmail.com', 'yahoo.com', 'outlook.com'];

    emailInput.addEventListener('input', function () {
        const emailValue = emailInput.value;

        // Check for basic email format validity
        if (!validateEmail(emailValue)) {
            emailError.style.display = 'block';
            emailError.textContent = 'Please enter a valid email address.';
        } else {
            emailError.style.display = 'none';
        }

        // Email domain suggestion
        const atIndex = emailValue.indexOf('@');
        if (atIndex > -1) {
            const enteredDomain = emailValue.slice(atIndex + 1);
            const suggestion = emailDomains.find(domain => domain.startsWith(enteredDomain));
            if (suggestion && suggestion !== enteredDomain) {
                emailSuggestion.style.display = 'block';
                emailSuggestion.textContent = emailValue.slice(0, atIndex + 1) + suggestion;
            } else {
                emailSuggestion.style.display = 'none';
            }
        } else {
            emailSuggestion.style.display = 'none';
        }
    });

    emailSuggestion.addEventListener('click', function () {
        emailInput.value = emailSuggestion.textContent;
        emailSuggestion.style.display = 'none';
    });

    document.getElementById('contactForm').addEventListener('submit', function (e) {
        if (!validateEmail(emailInput.value)) {
            e.preventDefault();
            emailError.style.display = 'block';
            emailError.textContent = 'Please enter a valid email address.';
        }
    });

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }
});

//Testimonial Swiper
var swiper = new Swiper
(
'.testimonial-swiper', 
  {
    slidesPerView: 1,
    spaceBetween: 0,
    loop: false,
    pagination: 
    {
      el: '.swiper-pagination',
      clickable: true,
    },
    navigation: 
    {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
  }
);

function handleSubmit(event) {
    event.preventDefault();
    
    // Gather form data
    const form = document.getElementById('contactForm');
    const formData = new FormData(form);

    fetch(form.action, {
        method: form.method,
        body: formData,
        headers: {
            'Accept': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            // Reset the form and clear any validation messages
            form.reset();
            document.getElementById('emailError').textContent = '';
            document.getElementById('emailSuggestion').textContent = '';

            // Show success message or popup
            alert("Thank you for reaching out! Your message has been sent successfully.");

            // Display the custom popup and blur effect
            showPopup();
            document.querySelector('.contact-content').classList.add('blurred');

        } else {
            alert("There was an issue with your submission. Please try again.");
        }
    }).catch(error => {
        console.error('Error:', error);
        alert("There was an error submitting your form. Please try again.");
    });

    return false;
}





function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tab-content");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tab-link");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

// Default open tab
document.getElementById("repairs").style.display = "block";


function showPopup() {
    document.getElementById('confirmationPopup').style.display = 'flex';
}

function closePopup() {
    // Hide the popup
    document.getElementById('confirmationPopup').style.display = 'none';

    // Remove the blur from the background
    document.querySelector('.contact-content').classList.remove('blurred');
}


document.getElementById("contactForm").addEventListener("submit", function (e) {
    e.preventDefault(); // prevent the default form submission
    let email = document.getElementById("email").value;
    let emailError = document.getElementById("emailError");
    let emailSuggestion = document.getElementById("emailSuggestion");

    if (!validateEmail(email)) {
        emailError.textContent = "Please enter a valid email address.";
        return;
    } else {
        emailError.textContent = "";
    }

    // After successful validation and form submission
    document.getElementById("contactForm").reset(); // clear the form
    document.getElementById("successMessage").style.display = "block"; // show the success message
});

function validateEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}
