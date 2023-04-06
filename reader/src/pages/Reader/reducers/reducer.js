const initialState = {
    isLoading: false,
    isError: false,
    library: [
        {
            "id": 5,
            "name": "Лісова пісня",
            "price": 128.0,
            "language": "Українська",
            "author": "Леся Українка",
            "publicationYear": "2022",
            "pages": 120,
            "type": "ELECTRONIC",
            "isbn": "9780674291874"
        },
    ],
};

export default (state = initialState, action) => {
    switch (action.type) {
        case 'ERROR_RECEIVE_BOOKS': {
            return {
                ...state,
                isLoading: false,
                isError: true,
                errorText: action.errorTextFromBE,
            };
        }
        case 'REQUEST_BOOKS': {
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        }
        case 'RECEIVE_BOOKS': {
            const {
                books,
            } = action;
            console.log("RECEIVE_BOOKS");
            return {
                ...state,
                isLoading: false,
                isError: false,
                library: books,
            };
        }
        default:
            console.log("default");
            return state;
    }
};