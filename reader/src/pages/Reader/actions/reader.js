const receiveBooks = books => ({
    books,
    type: 'RECEIVE_BOOKS'
});

const requestBooks = () => ({
    type: 'REQUEST_BOOKS'
});

const errorReceiveBooks = () => ({
    type: 'ERROR_RECEIVE_BOOKS'
});

const getBooks = () => {
    console.log("In fetch")
    const url = `http://localhost:8080/api/books`;
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Authorization': 'Basic ' + btoa('api' + ':' + '789')
        },
    };
    return fetch(url, options);
};

export const fetchBooks = () => (dispatch) => {
    dispatch(requestBooks());
    return getBooks()
        .then(response => {
            if (response.ok){
                response.json()
                    .then(books => dispatch(receiveBooks(books)))
                    .catch(() => dispatch(errorReceiveBooks()));
            }
            else {
                console.log('Error status ' + response.status)
            }
        })
};

const getUser = ({email, password}) => {
  console.log(`In fetch ${email}`)
  const url = `http://localhost:8080/api/users`;
  const options = {
    method: 'POST',
    body: JSON.stringify({
      dish_name: `${email}`,
      weight: `${password}`
    }),
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    },
  };
  return fetch(url, options);
};

export const fetchGetUser = ({ email, password }) => (dispatch) => {
  return getUser({
    email,
    password,
  })
      .then(response => {
        if (response.ok){
          console.log("Logged in");
          dispatch(fetchBooks())
        }
        else {
          console.log('Error status ' + response.status)
        }
      })
};

const getBookById = (bookId) => {
    console.log(`In fetch ${bookId}`)
    const url = `http://localhost:8080/api/books/${bookId}`;
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
    };
    return fetch(url, options);
};

export const fetchBookById = ({ bookId }) => (dispatch) => {
    dispatch(requestBooks());
    return getBookById(bookId)
        .then(response => {
            if (response.ok){
                response.json()
                    .then(books => dispatch(receiveBooks(books)))
                    .catch(() => dispatch(errorReceiveBooks()));
            }
            else {
                console.log('Error status ' + response.status)
            }
        })
};