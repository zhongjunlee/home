package com.johnnylee.cloud.module.system.service.notice;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.system.controller.admin.notice.vo.NoticeCreateReqVO;
import com.johnnylee.cloud.module.system.controller.admin.notice.vo.NoticePageReqVO;
import com.johnnylee.cloud.module.system.controller.admin.notice.vo.NoticeUpdateReqVO;
import com.johnnylee.cloud.module.system.convert.notice.NoticeConvert;
import com.johnnylee.cloud.module.system.dal.dataobject.notice.NoticeDO;
import com.johnnylee.cloud.module.system.dal.mysql.notice.NoticeMapper;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.johnnylee.cloud.module.system.enums.ErrorCodeConstants.NOTICE_NOT_FOUND;

/**
 * 通知公告 Service 实现类
 *
 * @author Johnny
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public Long createNotice(NoticeCreateReqVO reqVO) {
        NoticeDO notice = NoticeConvert.INSTANCE.convert(reqVO);
        noticeMapper.insert(notice);
        return notice.getId();
    }

    @Override
    public void updateNotice(NoticeUpdateReqVO reqVO) {
        // 校验是否存在
        validateNoticeExists(reqVO.getId());
        // 更新通知公告
        NoticeDO updateObj = NoticeConvert.INSTANCE.convert(reqVO);
        noticeMapper.updateById(updateObj);
    }

    @Override
    public void deleteNotice(Long id) {
        // 校验是否存在
        validateNoticeExists(id);
        // 删除通知公告
        noticeMapper.deleteById(id);
    }

    @Override
    public PageResult<NoticeDO> getNoticePage(NoticePageReqVO reqVO) {
        return noticeMapper.selectPage(reqVO);
    }

    @Override
    public NoticeDO getNotice(Long id) {
        return noticeMapper.selectById(id);
    }

    @VisibleForTesting
    public void validateNoticeExists(Long id) {
        if (id == null) {
            return;
        }
        NoticeDO notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw exception(NOTICE_NOT_FOUND);
        }
    }

}
