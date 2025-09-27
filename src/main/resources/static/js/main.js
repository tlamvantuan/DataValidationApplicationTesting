// Main JavaScript for Date Validation System

// Document ready
document.addEventListener('DOMContentLoaded', function() {
    console.log('Date Validation System loaded');

    // Initialize forms
    initializeForms();

    // Set current year as default
    const currentYear = new Date().getFullYear();
    const yearInputs = document.querySelectorAll('input[name="year"]');
    yearInputs.forEach(input => {
        if (!input.value) {
            input.placeholder = `VD: ${currentYear}`;
        }
    });
});

// Initialize form handlers
function initializeForms() {
    // Date validation form
    const dateForm = document.getElementById('dateForm');
    if (dateForm) {
        dateForm.addEventListener('submit', handleDateValidation);
    }

    // Days calculation form
    const daysForm = document.getElementById('daysForm');
    if (daysForm) {
        daysForm.addEventListener('submit', handleDaysCalculation);
    }
}

// Handle date validation form submission
async function handleDateValidation(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const day = parseInt(formData.get('day'));
    const month = parseInt(formData.get('month'));
    const year = parseInt(formData.get('year'));

    if (!day || !month || !year) {
        showAlert('Vui lòng nhập đầy đủ thông tin!', 'warning');
        return;
    }

    try {
        showLoading('result');
        const response = await fetch('/validate-date', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `day=${day}&month=${month}&year=${year}`
        });

        const result = await response.json();
        displayValidationResult(result);

    } catch (error) {
        console.error('Error:', error);
        showAlert('Có lỗi xảy ra khi validate ngày!', 'danger');
    }
}

// Handle days calculation form submission
async function handleDaysCalculation(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const month = parseInt(formData.get('month'));
    const year = parseInt(formData.get('year'));

    if (!month || !year) {
        showAlert('Vui lòng nhập đầy đủ thông tin!', 'warning', 'calcResult');
        return;
    }

    try {
        showLoading('calcResult');
        const response = await fetch('/calculate-days', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `month=${month}&year=${year}`
        });

        const result = await response.json();
        displayDaysResult(result);

    } catch (error) {
        console.error('Error:', error);
        showAlert('Có lỗi xảy ra khi tính số ngày!', 'danger', 'calcResult');
    }
}

// Display validation result
function displayValidationResult(result) {
    const resultDiv = document.getElementById('result');
    const alertDiv = document.getElementById('resultAlert');
    const messageDiv = document.getElementById('resultMessage');
    const detailsDiv = document.getElementById('resultDetails');

    // Set alert type based on validation result
    alertDiv.className = result.valid ? 'alert alert-success' : 'alert alert-danger';

    // Set message
    messageDiv.textContent = result.message;

    // Set details
    const leapYear = isLeapYear(result.year);
    const detailsHTML = `
        <div class="row">
            <div class="col-md-6">
                <strong>Thông tin chi tiết:</strong>
                <ul class="list-unstyled mt-2">
                    <li><i class="fas fa-calendar-day me-2"></i>Ngày: ${result.day}</li>
                    <li><i class="fas fa-calendar me-2"></i>Tháng: ${result.month}</li>
                    <li><i class="fas fa-clock me-2"></i>Năm: ${result.year}</li>
                </ul>
            </div>
            <div class="col-md-6">
                <strong>Thông tin bổ sung:</strong>
                <ul class="list-unstyled mt-2">
                    <li><i class="fas ${leapYear ? 'fa-check text-success' : 'fa-times text-danger'} me-2"></i>
                        ${leapYear ? 'Năm nhuận' : 'Năm thường'}</li>
                    <li><i class="fas fa-calendar-alt me-2"></i>
                        Số ngày trong tháng: ${getDaysInMonth(result.month, result.year)}</li>
                </ul>
            </div>
        </div>
    `;

    detailsDiv.innerHTML = detailsHTML;

    // Show result with animation
    resultDiv.style.display = 'block';
    resultDiv.classList.add('fade-in');
}

// Display days calculation result
function displayDaysResult(result) {
    const resultDiv = document.getElementById('calcResult');
    const alertDiv = document.getElementById('calcResultAlert');
    const messageDiv = document.getElementById('calcResultMessage');
    const detailsDiv = document.getElementById('calcResultDetails');

    // Set alert type
    alertDiv.className = result.days > 0 ? 'alert alert-success' : 'alert alert-danger';

    // Set message
    messageDiv.textContent = result.message;

    // Set details
    if (result.days > 0) {
        const detailsHTML = `
            <div class="row">
                <div class="col-md-6">
                    <strong>Chi tiết tháng:</strong>
                    <ul class="list-unstyled mt-2">
                        <li><i class="fas fa-calendar me-2"></i>${result.monthName}</li>
                        <li><i class="fas fa-clock me-2"></i>Năm ${result.year}</li>
                        <li><i class="fas fa-calendar-day me-2"></i>${result.days} ngày</li>
                    </ul>
                </div>
                <div class="col-md-6">
                    <strong>Thông tin năm:</strong>
                    <ul class="list-unstyled mt-2">
                        <li><i class="fas ${result.leapYear ? 'fa-check text-success' : 'fa-times text-danger'} me-2"></i>
                            ${result.leapYear ? 'Năm nhuận' : 'Năm thường'}</li>
                        ${result.month === 2 ? `<li><i class="fas fa-info-circle text-info me-2"></i>
                            Tháng 2 có ${result.days} ngày</li>` : ''}
                    </ul>
                </div>
            </div>
        `;
        detailsDiv.innerHTML = detailsHTML;
    } else {
        detailsDiv.innerHTML = '';
    }

    // Show result with animation
    resultDiv.style.display = 'block';
    resultDiv.classList.add('fade-in');
}

// Utility functions
function showLoading(containerId) {
    const container = document.getElementById(containerId);
    container.style.display = 'block';
    container.innerHTML = '<div class="text-center"><div class="spinner"></div><p>Đang xử lý...</p></div>';
}

function showAlert(message, type, containerId = 'result') {
    const container = document.getElementById(containerId);
    container.style.display = 'block';
    container.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
}

function isLeapYear(year) {
    return (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
}

function getDaysInMonth(month, year) {
    const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    if (month === 2 && isLeapYear(year)) {
        return 29;
    }
    return daysInMonth[month - 1];
}

// Test functions for sample cases
function testCase(day, month, year) {
    document.getElementById('day').value = day;
    document.getElementById('month').value = month;
    document.getElementById('year').value = year;

    // Trigger form submission
    document.getElementById('dateForm').dispatchEvent(new Event('submit'));
}

function quickCalc(month, year) {
    document.getElementById('calcMonth').value = month;
    document.getElementById('calcYear').value = year;

    // Trigger form submission
    document.getElementById('daysForm').dispatchEvent(new Event('submit'));
}

// Clear form functions
function clearForm() {
    document.getElementById('dateForm').reset();
    document.getElementById('result').style.display = 'none';
}

function clearCalcForm() {
    document.getElementById('daysForm').reset();
    document.getElementById('calcResult').style.display = 'none';
}

// Test today function for homepage
async function testToday() {
    const today = new Date();
    const day = today.getDate();
    const month = today.getMonth() + 1;
    const year = today.getFullYear();

    try {
        const response = await fetch('/validate-date', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `day=${day}&month=${month}&year=${year}`
        });

        const result = await response.json();
        const resultDiv = document.getElementById('todayResult');

        resultDiv.innerHTML = `
            <div class="alert alert-success">
                <strong>${result.message}</strong>
                <br><small class="text-muted">Hôm nay: ${day}/${month}/${year}</small>
            </div>
        `;

    } catch (error) {
        console.error('Error:', error);
        document.getElementById('todayResult').innerHTML =
            '<div class="alert alert-danger">Có lỗi xảy ra!</div>';
    }
}

// Add some interactive effects
document.addEventListener('DOMContentLoaded', function() {
    // Add hover effects to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Add click effects to buttons
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(button => {
        button.addEventListener('click', function() {
            this.classList.add('pulse');
            setTimeout(() => {
                this.classList.remove('pulse');
            }, 600);
        });
    });
});