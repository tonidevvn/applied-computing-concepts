import { Input, Table, Typography } from 'antd'
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
        {
            dataIndex: 'description',
            title: 'Description',
        },
    ]

    return (
        <div>
            <Typography.Title>Inverted Indexing</Typography.Title>
            <Table dataSource={data} columns={options}></Table>
        </div>
    )
}

export default InvertedIndex
