import { Card, Input, Table, Typography } from 'antd'
import React, { useEffect, useState } from 'react'
import { InvertedIndexType } from '../types/invertedindex'

function InvertedIndex({ data }: { data: InvertedIndexType[] }) {
    const options = [
        {
            dataIndex: 'docId',
            title: 'Document ID',
        },
        {
            dataIndex: 'name',
            title: 'Name',
        },
    ]

    return (
        <Card title='Inverted Indexing'>
            {data.length !== 0 ? (
                <Table dataSource={data} columns={options}></Table>
            ) : (
                <div>No Data</div>
            )}
        </Card>
    )
}

export default InvertedIndex
