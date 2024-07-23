'use client'

import React, { useEffect } from 'react'
import { WebCrawlerProps } from '../types/webcrawler'
import axios from 'axios'
import { Alert, Button, Card, Form, Input, message, Skeleton } from 'antd'
type FieldType = {
    url?: string
}
function WebCrawler() {
    const [loading, setLoading] = React.useState(false)
    const [error, setError] = React.useState('')
    const [result, setResult] = React.useState<WebCrawlerProps>(
        {} as WebCrawlerProps
    )

    const fetchData = async (values: FieldType) => {
        setError('')
        setLoading(true)
        setResult({} as WebCrawlerProps)
        await axios
            .get('/api/web-crawler', {
                params: values,
            })
            .then((res) => {
                setResult(res.data)
            })
            .catch((err) => {
                setError('Invalid URL. Please try again')
                setResult({} as WebCrawlerProps)
            })
            .finally(() => {
                setLoading(false)
            })
    }
    return (
        <div className='min-h-screen flex flex-col items-center justify-center p-4'>
            <Card title='Web Crawler' className='w-1/2 '>
                <Form
                    name='basic'
                    labelCol={{ span: 3 }}
                    initialValues={{ remember: true }}
                    onFinish={fetchData}
                    // onFinishFailed={onFinishFailed}
                    autoComplete='off'
                >
                    <Form.Item<FieldType>
                        label='URL'
                        name='url'
                        rules={[
                            {
                                required: true,
                                message: 'Please input your url!',
                            },
                        ]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item className='text-center'>
                        <Button type='primary' htmlType='submit'>
                            Submit
                        </Button>
                    </Form.Item>
                </Form>
                {loading && <Skeleton loading />}
                {error && <Alert message={error} type='error' />}
                {result.time && (
                    <div className='mt-6'>
                        <p className='font-semibold'>Time:</p>
                        <p className='mb-4'>{result.time}</p>
                        <p className='font-semibold'>Content:</p>
                        <p>{result.htmlContents}</p>
                    </div>
                )}
            </Card>
        </div>
    )
}

export default WebCrawler
