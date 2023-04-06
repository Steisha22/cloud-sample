import React, {useEffect, useState} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory, useParams} from "react-router-dom";
import Button from 'components/Button';

import {
    fetchBookById, fetchBooks
} from "pages/Reader/actions/reader";


const getClasses = makeStyles(() => ({
    container: {
        display: 'flex',
        flexDirection: 'column',
        width: '100%',
    },
}));

const initialState = {
    "id": '',
    "name": "",
    "author": "",
    "pages": '',
    "text": ""
};

function Book(){
    const params = useParams();
    const current = params.id;
    const history = useHistory();
    const library = useSelector(({ library }) => library);
    const dispatch = useDispatch();
    const [state, setState] = useState(initialState);
    const classes = getClasses();

    useEffect(() => {
        console.log("current" + current);
        dispatch(fetchBookById({
            bookId: current,
        }));
        console.log(library);
    }, []);

    useEffect(() => {
        setState(library.library);
    }, [library]);

    return (
        <div className={classes.container}>
            <h1>Book id {state.id}     Name {state.name}</h1>
            <h4>{state.author}</h4>
            <p>{state.pages} pages</p>
            <div>
                <p>{state.text}</p>
            </div>
            <Button style={{
                height: 50,
                width: 200,
                backgroundColor: "red",
            }} onClick={() => {history.goBack(); dispatch(fetchBooks())}}>Return</Button>
        </div>
    )
}

export default Book;
