import React, {useEffect, useState} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {useHistory, useParams} from "react-router-dom";
import Button from 'components/Button';
import TextField from 'components/TextField';
import {useDispatch, useSelector} from "react-redux";
//import List from 'components/List';
//import ListItemText from 'components/ListItemText';
//import Divider from 'components/Divider';

import {
    fetchGetUser,
} from "../actions/reader";


const getClasses = makeStyles(() => ({
    container: {
        display: 'flex',
        flexDirection: 'column',
        width: '100%',
    },
}));

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(2),
            width: '55ch',
        },
    },
}));

const style = {
    width: '100%',
    maxWidth: 360,
};

const style2 = {
    paddingLeft: 2,
};

const initialState = {
    email: "",
    password: ""
};

function Reader(){
    const classes = getClasses();
    const classes2 = useStyles();
    const history = useHistory();
    const dispatch = useDispatch();
    const [state, setState] = useState(initialState);

    function login(){
        // await dispatch(fetchGetUser({
        //     email: state.email,
        //     password: state.password,
        //     }));
        //console.log("saveChanges");
        history.push('/reader/books');
    }

    return (
        <div className={classes.container}>
            <h1>Welcome to reader!!!</h1>
            <h3>To see your books please enter your email and password from Library service to the form below</h3>
            <form className={classes2.root} noValidate autoComplete="off">
                <div>
                    <h1>Login into Library service</h1>
                    <TextField
                        fullWidth
                        inputType="text"
                        label='email'
                        variant="outlined"
                        onChange={({ target }) => setState(prevState => ({
                            ...prevState,
                            email: target.value,
                        }))}
                    />
                    <TextField
                        fullWidth
                        inputType="text"
                        label='password'
                        variant="outlined"
                        onChange={({ target }) => setState(prevState => ({
                            ...prevState,
                            password: target.value,
                        }))}
                    />
                </div>
            </form>
            <div style={{
                margin: 30,
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'space-around'
            }}>
                <Button style={{
                    height: 50,
                    width: 200,
                    backgroundColor: "#27d827",
                }} onClick={login}>Log in</Button>
            </div>
        </div>
    )
};

export default Reader;
