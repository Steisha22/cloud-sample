import React from 'react';
import PageAccessValidator from 'components/PageAccessValidator';
import BookPage from 'pages/Book';
import PageContainer from 'components/PageContainer';

const Book = () => (
    <PageAccessValidator>
        <PageContainer>
            <BookPage />
        </PageContainer>
    </PageAccessValidator>
);

export default Book;
