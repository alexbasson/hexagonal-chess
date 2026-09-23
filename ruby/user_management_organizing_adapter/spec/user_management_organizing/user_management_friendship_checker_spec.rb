require 'user_management/user_id'
require 'user_management/friendship'
require 'user_management/friendship_repository'
require 'organizing/friendship_checker'
require 'user_management_organizing/user_management_friendship_checker'

RSpec.describe UserManagementOrganizing::UserManagementFriendshipChecker do
  subject(:checker) { described_class.new(friendship_repository) }

  let(:friendship_repository) { instance_double(UserManagement::FriendshipRepository) }
  let(:owner_id)              { UserManagement::UserId.new(value: 'u1') }
  let(:friend_id)             { UserManagement::UserId.new(value: 'u2') }
  let(:friendship)            { UserManagement::Friendship.new(owner_id: owner_id, friend_id: friend_id) }

  it 'returns true when friendship exists' do
    allow(friendship_repository).to receive(:find_by_owner_id).with(owner_id).and_return([friendship])

    expect(checker.are_friends?('u1', 'u2')).to be true
  end

  it 'returns false when no friendship' do
    allow(friendship_repository).to receive(:find_by_owner_id).with(owner_id).and_return([])

    expect(checker.are_friends?('u1', 'u2')).to be false
  end
end
