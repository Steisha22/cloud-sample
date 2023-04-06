import React from 'react';
import PageAccessValidator from 'components/PageAccessValidator';
import ReaderPage from 'pages/Reader';
import PageContainer from 'components/PageContainer';

const Reader = () => (
    <PageAccessValidator>
        <PageContainer>
            <ReaderPage />
        </PageContainer>
    </PageAccessValidator>
);

export default Reader;
