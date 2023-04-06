import React, {useEffect} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from "react-router-dom";
// import List from 'components/List';
// import ListItemText from 'components/ListItemText';
import Button from 'components/Button';
// import Divider from 'components/Divider';
import Table from 'components/Table';
import TableBody from 'components/TableBody';
import TableCell from 'components/TableCell';
import TableContainer from 'components/TableContainer';
import TableHead from 'components/TableHead';
import TableRow from 'components/TableRow';
import Paper from 'components/Paper';

import {
    fetchBooks,
} from "pages/Reader/actions/reader";


const getClasses = makeStyles(() => ({
    container: {
        display: 'flex',
        flexDirection: 'column',
        width: '100%',
    },
}));

function Books(){
    const history = useHistory();
    const library = useSelector(({ library }) => library);
    const dispatch = useDispatch();
    const classes = getClasses();

    useEffect(() => {
        console.log("ComponentDidMount");
        dispatch(fetchBooks());
        console.log(library);
    }, []);

    function readBook(id) {
        history.push(`/reader/books/${id}`);
    }

    return (
        <div className={classes.container}>
            <div style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'center'
            }}>
            </div>
            <h1>Books Page</h1>
            {!library.isLoading && library.library.map((item) => (
                <TableContainer component={Paper}>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Id</TableCell>
                                <TableCell align="right">Name</TableCell>
                                <TableCell align="right">Price</TableCell>
                                <TableCell align="right">Language</TableCell>
                                <TableCell align="right">Button</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                                <TableRow
                                    key={item.name}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                >
                                    <TableCell component="th" scope="row">
                                        {item.id}
                                    </TableCell>
                                    <TableCell align="right">{item.name}</TableCell>
                                    <TableCell align="right">{item.price}</TableCell>
                                    <TableCell align="right">{item.language}</TableCell>
                                    <TableCell align="right">
                                        <Button id={item.id} style={{marginRight: 100,}}
                                                onClick={() => readBook(item.id)} variant="contained">
                                            Read</Button>
                                    </TableCell>
                                </TableRow>
                        </TableBody>
                    </Table>
                </TableContainer>
                // <List sx={style}>
                //     <div style={{
                //         display: 'flex',
                //         flexDirection: 'row',
                //     }}>
                //         <ListItemText sx={style2} primary={`Book name is ${item.name}, its ID is ${item.id} `} />
                //         <Button id={item.id} style={{
                //             marginRight: 100,
                //         }} onClick={() => editDish(item.id)} variant="contained">Edit</Button>
                //         {/*<ListItemText sx={style2} primary={`Book name is ${item.book_name}, its weight is ${item.weight} grams,*/}
                //         {/*Price: ${item.price} UAH. ${item.dish_category}. Description: "${item.description}"`} />*/}
                //     </div>
                //     <Divider />
                // </List>
            ))}
        </div>
    )
};

export default Books;
