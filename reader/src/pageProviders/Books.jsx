import React from 'react';
import PageAccessValidator from 'components/PageAccessValidator';
import BooksPage from 'pages/Books';
import PageContainer from 'components/PageContainer';

const Books = () => (
    <PageAccessValidator>
        <PageContainer>
            <BooksPage />
        </PageContainer>
    </PageAccessValidator>
);

export default Books;
