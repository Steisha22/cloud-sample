import React from 'react';
import { applyMiddleware, createStore, combineReducers } from 'redux';
import { Provider } from 'react-redux';
import thunkMiddleware from 'redux-thunk';

import App from './containers/App.jsx';
import userReducer from './reducers/user';
import libraryReducer from '../pages/Reader/reducers/reducer';

const rootReducer = combineReducers({
  user: userReducer,
    library: libraryReducer,
});

const store = createStore(
  rootReducer,
  applyMiddleware(thunkMiddleware),
);

export default () => (
  <Provider store={store} >
    <App />
  </Provider>
)
