const API_BASE_URL = '/books';

// Load books on page load
document.addEventListener('DOMContentLoaded', () => {
    loadBooks();
    
    // Add book form submission
    document.getElementById('addBookForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await addBook();
    });
    
    // Payment type change handler
    const paymentTypeSelect = document.getElementById('paymentType');
    if (paymentTypeSelect) {
        paymentTypeSelect.addEventListener('change', togglePaymentFields);
        togglePaymentFields(); // Initialize on load
    }
});

// Toggle payment fields based on payment type
function togglePaymentFields() {
    const paymentType = document.getElementById('paymentType').value;
    const cardFields = document.getElementById('cardFields');
    const upiFields = document.getElementById('upiFields');
    
    if (paymentType === 'upi') {
        if (cardFields) cardFields.style.display = 'none';
        if (upiFields) upiFields.style.display = 'block';
    } else {
        if (cardFields) cardFields.style.display = 'block';
        if (upiFields) upiFields.style.display = 'none';
    }
}

// Load all books
async function loadBooks() {
    try {
        const response = await fetch(API_BASE_URL);
        const data = await response.json();
        
        if (data.success) {
            displayBooks(data.data);
        }
    } catch (error) {
        showMessage('Error loading books: ' + error.message, 'error');
    }
}

// Display books
function displayBooks(books) {
    const booksList = document.getElementById('booksList');
    
    if (books.length === 0) {
        booksList.innerHTML = '<p>No books available</p>';
        return;
    }
    
    booksList.innerHTML = books.map(book => {
        const safeName = book.name.replace(/\\/g, '\\\\').replace(/'/g, "\\'");
        return `
        <div class="book-item">
            <h3>${book.name}</h3>
            <p>Author: ${book.author}</p>
            <p>Price: ₹${book.price}</p>
            <button onclick="selectBookForPayment('${safeName}', ${book.price})" class="btn-small">Buy Now</button>
        </div>`;
    }).join('');
}

// Select book for payment
function selectBookForPayment(bookName, price) {
    document.getElementById('paymentBookName').value = bookName;
    document.getElementById('paymentAmount').value = price;
    showMessage('Book selected: ' + bookName + '. Please complete payment details below.', 'success');
    document.getElementById('paymentBookName').scrollIntoView({ behavior: 'smooth' });
}

// Add new book
async function addBook() {
    const bookData = {
        name: document.getElementById('bookName').value,
        author: document.getElementById('bookAuthor').value,
        price: parseFloat(document.getElementById('bookPrice').value)
    };
    
    if (bookData.price <= 0) {
        showMessage('Price must be greater than 0', 'error');
        return;
    }
    
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bookData)
        });
        
        const data = await response.json();
        
        if (data.success) {
            showMessage('Book added successfully!', 'success');
            document.getElementById('addBookForm').reset();
            loadBooks();
        } else {
            showMessage('Failed to add book', 'error');
        }
    } catch (error) {
        showMessage('Error: ' + error.message, 'error');
    }
}

// Process payment
async function processPayment() {
    const paymentType = document.getElementById('paymentType').value;
    const bookName = document.getElementById('paymentBookName').value;
    const amount = parseFloat(document.getElementById('paymentAmount').value);
    
    if (!bookName || !amount || amount <= 0) {
        showMessage('Please enter valid book name and amount', 'error');
        return;
    }
    
    const paymentData = {
        paymentType: paymentType,
        bookName: bookName,
        amount: amount
    };
    
    // Add payment-specific fields
    if (paymentType === 'upi') {
        const upiId = document.getElementById('upiId')?.value;
        if (!upiId) {
            showMessage('Please enter UPI ID', 'error');
            return;
        }
        paymentData.upiId = upiId;
    } else {
        // Card payment (CREDIT_CARD or DEBIT_CARD)
        const cardNumber = document.getElementById('cardNumber')?.value;
        const cardHolder = document.getElementById('cardHolder')?.value;
        const expiryDate = document.getElementById('expiryDate')?.value;
        const cvv = document.getElementById('cvv')?.value;
        
        if (!cardNumber || !cardHolder || !expiryDate || !cvv) {
            showMessage('Please fill all card details', 'error');
            return;
        }
        
        paymentData.cardNumber = cardNumber.replace(/\s+/g, '');
        paymentData.cardHolderName = cardHolder;
        paymentData.expiryDate = expiryDate;
        paymentData.cvv = cvv;
    }
    
    const endpoint = paymentType === 'upi'
        ? `${API_BASE_URL}/process-payment/upi`
        : `${API_BASE_URL}/process-payment/card`;

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(paymentData)
        });
        
        const result = await response.json();
        
        if (!response.ok && !result.success) {
            showMessage(`✗ Payment failed: ${result.message || 'Unexpected error'}`, 'error');
            return;
        }

        if (result.success) {
            showMessage(`✓ Payment successful! Transaction ID: ${result.transactionId}. Order placed for: ${bookName}`, 'success');
            // Clear form
            document.getElementById('paymentBookName').value = '';
            document.getElementById('paymentAmount').value = '';
            if (document.getElementById('upiId')) document.getElementById('upiId').value = '';
            if (document.getElementById('cardNumber')) document.getElementById('cardNumber').value = '';
            if (document.getElementById('cardHolder')) document.getElementById('cardHolder').value = '';
            if (document.getElementById('expiryDate')) document.getElementById('expiryDate').value = '';
            if (document.getElementById('cvv')) document.getElementById('cvv').value = '';
        } else {
            showMessage(`✗ Payment failed: ${result.message}`, 'error');
        }
    } catch (error) {
        showMessage('Error processing payment: ' + error.message, 'error');
    }
}

// Show message
function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = message;
    messageDiv.className = type;
    messageDiv.style.display = 'block';
    
    setTimeout(() => {
        messageDiv.style.display = 'none';
    }, 8000);
}
