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
        <div className='bg-white rounded-lg p-6'>
            <h1 className='text-3xl font-bold mb-4'>Inverted Indexing</h1>
            <Table dataSource={data} columns={options}></Table>
        </div>
    )
}

export default InvertedIndex
